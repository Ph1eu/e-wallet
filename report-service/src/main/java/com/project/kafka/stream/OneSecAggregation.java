package com.project.kafka.stream;

import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import jakarta.annotation.PostConstruct;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)

public class OneSecAggregation {
    private final Properties properties;
    private static final Logger logger = LoggerFactory.getLogger(GlobalAggregation.class);

    private final String topic;
    private KafkaStreams stream;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    @Autowired
    public OneSecAggregation(@Qualifier("kafkaOneSecAggregationProp") Properties properties,
                             @Value("${one_sec_transaction_aggregrated.topic}") String topic) {
        this.properties = properties;
        this.topic = topic;
        createTopicIfNotExists();
    }

    @PostConstruct
    public void initializeKafkaStreams() {
        // Initialize the Kafka Streams application during application startup
        startStream();
    }

    Schema.Parser parser = new Schema.Parser();
    Schema schema = parser.parse("{\n" +
            "    \"type\": \"record\",\n" +
            "    \"name\":\"Aggregated_Transaction\",\n" +
            "    \"fields\":[\n" +
            "        {\"name\":\"total_transaction_amount\", \"type\":\"long\"},\n" +
            "        {\"name\":\"total_record_count\", \"type\":\"int\"},\n" +
            "        {\"name\":\"start_time\", \"type\":\"long\"},\n" +
            "        {\"name\":\"end_time\", \"type\":\"long\"}\n" +
            "    ]\n" +
            "}");

    public Topology CreateStream() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, GenericRecord> transactionsStream = builder.stream("transaction",
                Consumed.with(Serdes.String(), valueGenericAvroSerde()));


// Perform aggregation for sum of transaction amounts with 1-second time windows
        KTable<Windowed<String>, Double> sumAggregatedTable = transactionsStream
                .mapValues(this::extractAmount)
                .filter((transactionId, data) -> data != null)
                .selectKey((key, value) -> "Aggregated result") // Assign a constant key for all records
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .windowedBy(TimeWindows.of(Duration.ofSeconds(1)))
                .reduce(
                        (totalAmount, data) -> totalAmount + data,
                        Materialized.with(Serdes.String(), Serdes.Double())
                );

        sumAggregatedTable.toStream().peek((key, value) -> System.out.println("key sum: " + key + " value sum: " + value));


// Perform aggregation for counting transactions with 1-second time windows
        KTable<Windowed<String>, Long> countAggregatedTable = transactionsStream
                .selectKey((key, value) -> "Aggregated result") // Assign a constant key for all records
                .groupByKey(Grouped.with(Serdes.String(), valueGenericAvroSerde()))
                .windowedBy(TimeWindows.of(Duration.ofSeconds(1)))
                .count(Materialized.with(Serdes.String(), Serdes.Long()));
        //countAggregatedTable.toStream().peek((key, value) -> System.out.println("key count: " + key + " value count: " + value));

        KTable<Windowed<String>, GenericRecord> finalTable = sumAggregatedTable
                .join(
                        countAggregatedTable,
                        (sum, count) -> {

                            GenericRecord result = new GenericData.Record(schema);
                            result.put("total_transaction_amount", sum);
                            result.put("total_record_count", count);

                            return result;
                        }
                );
        finalTable.toStream().peek((key, value) -> System.out.println("key : " + key + " value : " + value));

        KStream<String, GenericRecord> finalStream = finalTable.toStream()
                .map((windowedTransactionId, result) -> {
                    GenericRecord record = new GenericData.Record(schema);
                    record.put("total_transaction_amount", result.get("total_transaction_amount"));
                    record.put("total_record_count", result.get("total_record_count"));
                    record.put("start_time", windowedTransactionId.window().start());
                    record.put("end_time", windowedTransactionId.window().end());

                    return KeyValue.pair(windowedTransactionId.key(), record);
                });
        // Map the merged stream to the final format


        finalStream.to(this.topic, Produced.with(Serdes.String(), valueGenericAvroSerde()));

        return builder.build();
    }

    public void startStream() {
        Topology topology = CreateStream();
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
            int maxWaitTimeMs = 300000; // 300 seconds
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


    private void createTopicIfNotExists() {
        try (AdminClient adminClient = AdminClient.create(this.properties)) {
            // Check if the topic already exists
            if (!adminClient.listTopics().names().get().contains(this.topic)) {
                // Create the topic with the desired configuration
                int numPartitions = 3; // Replace with the desired partition count
                short replicationFactor = 1; // Replace with the desired replication factor

                NewTopic newTopic = new NewTopic(this.topic, numPartitions, replicationFactor);
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
    private String generateUniqueKey(Windowed<String> windowedKey, double sumValue, long countValue) {
        return windowedKey.key() + "|" + sumValue + "|" + countValue;
    }

//    private KStream<String, GenericRecord> generateContinuousRecords() {
//        StreamsBuilder builder = new StreamsBuilder();
//        // Continuous record generation with a timestamp within 1-second time windows
//        KStream<String, GenericRecord> dummyStream = builder.stream("transaction");
//
//        // Transform the dummy stream using the punctuate function to emit a continuous record with the current timestamp
//        return dummyStream.transform(() -> new ContinuousRecordGenerator(), Named.as("ContinuousRecordGenerator"));
//    }

//    private  class ContinuousRecordGenerator implements Transformer<String, GenericRecord, KeyValue<String, GenericRecord>> {
//
//        private ProcessorContext context;
//
//        @Override
//        public void init(ProcessorContext context) {
//            this.context = context;
//            this.context.schedule(Duration.ofSeconds(1), PunctuationType.WALL_CLOCK_TIME, this::punctuate);
//
//        }
//
//        @Override
//        public KeyValue<String, GenericRecord> transform(String key, GenericRecord value) {
//            // No need to emit records here since we emit them in the punctuate method
//            return null;
//        }
//        private void punctuate(long timestamp) {
//            // Emit a continuous record with the current timestamp
//            GenericRecord record = new GenericData.Record(schema);
//            record.put("total_transaction_amount", 0.0);
//            record.put("total_record_count", 0L);
//            this.context.forward("Aggregated result", record);
//            this.context.commit(); // Commit the punctuated record
//        }
//        @Override
//        public void close() {
//            // Cleanup if needed
//        }
//    }
}