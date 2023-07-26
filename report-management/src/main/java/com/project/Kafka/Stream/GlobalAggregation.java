package com.project.Kafka.Stream;

import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import jakarta.annotation.PostConstruct;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GlobalAggregation {
    private final Properties properties;
    private static final Logger logger = LoggerFactory.getLogger(GlobalAggregation.class);

    private final String topic; // The Kafka topic to which messages will be sent
    private KafkaStreams stream; // Hold the active Kafka Streams instance


    @Autowired
    public GlobalAggregation(@Qualifier("kafkaGlobalAggregationProp") Properties properties,
                             @Value("${global_transaction_aggregrated.topic}") String topic) {
        this.properties = properties;
        this.topic = topic;
        createTopicIfNotExists();
    }


    public Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, GenericRecord> transactionsStream = builder.stream("transaction",
                Consumed.with(Serdes.String(), valueGenericAvroSerde()));

        // Perform aggregation for sum of transaction amounts
        KTable<String, Double> sumAggregatedTable = transactionsStream
                .mapValues(this::extractAmount)
                .filter((transactionId, data) -> data != null)
                .selectKey((key, value) -> "Aggregated result") // Assign a constant key for all records
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .reduce(
                        (totalAmount, data) -> totalAmount + data,
                        Materialized.with(Serdes.String(), Serdes.Double())
                );
        // Perform aggregation for counting transactions
        KTable<String, Long> countAggregatedTable = transactionsStream.
                selectKey((key, value) -> "Aggregated result") // Assign a constant key for all records
                .groupByKey(Grouped.with(Serdes.String(), valueGenericAvroSerde()))
                .count(Materialized.with(Serdes.String(), Serdes.Long()));
        // Merge the two aggregated tables into one
        KTable<String, String> mergedTable = sumAggregatedTable
                .join(
                        countAggregatedTable,
                        (sum, count) -> "Total Transaction Amount: " + sum + ", Total Record Count: " + count
                )
                .mapValues(result -> "Global " + result);

        KStream<String, String> mergedStream = mergedTable.toStream();

        // Map the merged stream to the final format
        mergedStream.peek((key, value) -> System.out.println("key: " + key + " value: " + value));

        mergedStream.to(this.topic, Produced.with(Serdes.String(), Serdes.String()));

        return builder.build();
    }
    public void startStream(){
        Topology topology = createTopology();
        this.stream = new KafkaStreams(topology, this.properties);
        this.stream.setUncaughtExceptionHandler((thread, throwable) -> logger.error("Error in Kafka Streams application", throwable));
        this.stream.setStateListener((newState, oldState) -> logger.info("State change from " + oldState + " to " + newState));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down Kafka Streams application...");
            this.stream.close();
            logger.info("Kafka Streams application has been shut down.");
        }));
        try {
            this.stream.start();
            int maxWaitTimeMs = 30000; // 30 seconds
            int pollIntervalMs = 100; // 100 milliseconds
            int timeWaitedMs = 0;

            while (this.stream.state() != KafkaStreams.State.RUNNING && timeWaitedMs < maxWaitTimeMs) {
                Thread.sleep(pollIntervalMs);
                timeWaitedMs += pollIntervalMs;
            }

            if (this.stream.state() != KafkaStreams.State.RUNNING) {
                logger.error("KafkaStreams application didn't enter the RUNNING state within the specified timeout.");
                this.stream.close();
            }

            logger.info("KafkaStreams application is now running.");
        } catch (Throwable e) {
            logger.error("Error occurred while starting the Kafka Streams application", e);
            this.stream.close();
        }

    }
    private boolean isKafkaStreamsRunning() {
        return this.stream != null && this.stream.state() == KafkaStreams.State.RUNNING;
    }


    private void createTopicIfNotExists() {
        try (AdminClient adminClient = AdminClient.create(this.properties)) {
            // Check if the topic already exists
            if (!adminClient.listTopics().names().get().contains(topic)) {
                // Create the topic with the desired configuration
                int numPartitions = 3; // Replace with the desired partition count
                short replicationFactor = 1; // Replace with the desired replication factor

                NewTopic newTopic = new NewTopic(topic, numPartitions, replicationFactor);
                adminClient.createTopics(Collections.singleton(newTopic)).all().get();
                System.out.println("Topic '" + topic + "' created.");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Handle the exception accordingly
        }
    }

    private Serde<GenericRecord> valueGenericAvroSerde() {
        final GenericAvroSerde valueGenericAvroSerde = new GenericAvroSerde();
        final Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url",
                this.properties.getProperty("schema.registry.url"));
        valueGenericAvroSerde.configure(serdeConfig, false);
        return valueGenericAvroSerde;
    }

    private Double extractAmount(GenericRecord genericRecord) {

        // Get the Avro schema for the GenericRecord
        Schema schema = genericRecord.getSchema();

        // Get the field corresponding to the "amount" field in the schema
        Schema.Field amountField = schema.getField("amount");

        // If the "amount" field is not present in the schema or it's null, return null
        if (amountField == null || genericRecord.get("amount") == null) {
            return null;
        }
        Object amountValue = genericRecord.get("amount");

        // If the "amount" field value is an instance of Utf8 (String), convert it to Double
        if (amountValue instanceof Utf8) {
            try {
                return Double.parseDouble(amountValue.toString());
            } catch (NumberFormatException e) {
                // Return null if parsing fails
                return null;
            }
        }
        if (amountValue instanceof Double) {
            return (Double) amountValue;
        }
        if (amountValue instanceof Integer) {
            return ((Integer) amountValue).doubleValue();
        }
        return null;
    }



    private boolean isIn5SecondsWindow(Long transactionDate) {
        Instant now = Instant.now();
        Instant transactionInstant = Instant.ofEpochMilli(transactionDate);
        Instant fiveSecondsAfterTransaction = transactionInstant.plusSeconds(5);
        return now.isBefore(fiveSecondsAfterTransaction);
    }

    private Long extractTransactionDate(GenericRecord genericRecord) {
        // Assuming you have the logic to extract transaction_date field from genericRecord
        // Example: If your transaction_date field is named "transaction_date" in the Avro schema
        return (Long) genericRecord.get("transaction_date");
    }


}