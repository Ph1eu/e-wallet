package com.project.Kafka.Stream;

import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;

import org.apache.kafka.streams.state.WindowStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)

public class OneSecAggregration {
    private final Properties properties;
    private static final Logger logger = LoggerFactory.getLogger(OneSecAggregration.class);

    private final String topic; // The Kafka topic to which messages will be sent

    @Autowired
    public OneSecAggregration(@Qualifier("kafkaOneSecAggregationProp")Properties properties,
                               @Value("${transaction_aggregrated.topic}") String topic) {
        this.properties = properties;
        this.topic = topic;
        createTopicIfNotExists();
    }
    public void stream(){
        StreamsBuilder builder = new StreamsBuilder();
       // System.out.print(this.properties);
        // Read from the "transactions" topic with Serdes.String() for the key
        KStream<String, GenericRecord> transactionsStream = builder.stream("transaction",
                Consumed.with(Serdes.String(), valueGenericAvroSerde()));
        transactionsStream
               // peek((key, value) -> logger.info("Received record: Key=" + key + ", Value=" + value))

                .mapValues(genericRecord -> extractAmount(genericRecord))
                .filter((transactionId, amount) -> amount != null)
                .groupByKey()
               /// .windowedBy(TimeWindows.of(Duration.of(1))) // 1-second window
                .aggregate(
                        () -> 0.0, // initial value for amount
                        (transactionId, amount, totalAmount) -> totalAmount +amount ,
                        Materialized.with(Serdes.String(),Serdes.Double())
                )
                .toStream()
//                .map((windowedTransactionId, totalAmount) -> KeyValue.pair(
//                        windowedTransactionId.key(),
//                        "1-second Total Amount: " + totalAmount + ", Start Time: " + windowedTransactionId.window().start() +
//                                ", End Time: " + windowedTransactionId.window().end()
//                ))
                .peek((key,value) -> System.out.println("key :" +key+ " value:" +value))
                .to(this.topic);



        KafkaStreams streams = new KafkaStreams(builder.build(), this.properties);
        streams.setUncaughtExceptionHandler((thread, throwable) -> logger.error("Error in Kafka Streams application", throwable));
        streams.setStateListener((newState, oldState) -> logger.info("State change from " + oldState + " to " + newState));

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        try {
            streams.start();
            while (streams.state() != KafkaStreams.State.RUNNING) {
                Thread.sleep(100); // Wait for the KafkaStreams to be in RUNNING state
            }
            logger.info("KafkaStreams application is now running.");
        } catch (Throwable e) {
            logger.error("Error occurred while starting the Kafka Streams application", e);
        }



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
}
