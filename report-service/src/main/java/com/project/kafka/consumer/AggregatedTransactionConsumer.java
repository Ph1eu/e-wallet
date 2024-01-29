package com.project.kafka.consumer;

import com.project.payload.dto.WindowAggregatedResultDTO;
import com.project.service.WindowAggregatedResultService;
import jakarta.annotation.PreDestroy;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AggregatedTransactionConsumer {
    private final Properties properties;
    private final String OneSectopic; // The Kafka topic to which messages will be sent
    private final String Globaltopic;
    private  KafkaConsumer<String, GenericRecord> kafkaConsumer;
    private GenericRecord latestRecord;
    private volatile boolean running; // To control the consumer loop
    private BlockingQueue<GenericRecord> recordQueue = new ArrayBlockingQueue<>(1000); // Adjust the size as needed
    private Thread kafkaConsumerThread;
    private Thread dbInsertionThread;
    @Autowired
    WindowAggregatedResultService windowAggregatedResultService;
    @Autowired
    private Executor executor;
    @Autowired
    public AggregatedTransactionConsumer(@Qualifier("kafkaTransactionConsumerProp")Properties properties,
                               @Value("${one_sec_transaction_aggregrated.topic}") String OneSectopic,
                                         @Value("${global_transaction_aggregrated.topic}") String Globaltopic) {
        this.properties = properties;
        this.OneSectopic = OneSectopic;
        this.Globaltopic = Globaltopic;
     //   this.kafkaConsumer = createConsumer();
        this.latestRecord = null;
        this.running = false;
    }
    public void init() {
        this.kafkaConsumer = createConsumer();
        this.running = true;
        this.kafkaConsumer.subscribe(Collections.singletonList(this.OneSectopic));

        kafkaConsumerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, GenericRecord> consumerRecords = this.kafkaConsumer.poll(Duration.ofSeconds(5));
                consumerRecords.forEach(record -> {
                    latestRecord = record.value();
                    recordQueue.offer(record.value()); // Add records to the queue
                    System.out.println("Record.value = " + record.value() + " ,Partition=" + record.partition());
                });

                this.kafkaConsumer.commitSync();
            }
            this.kafkaConsumer.close();
        });
        dbInsertionThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    GenericRecord record = recordQueue.poll(); // Take records from the queue
                    if (record != null) {
                        // Insert logic - Modify this according to your data structure
                        WindowAggregatedResultDTO windowAggregatedResultDTO = new WindowAggregatedResultDTO(
                                UUID.randomUUID().toString(),
                                Double.parseDouble(record.get("total_transaction_amount").toString()),
                                Integer.parseInt(record.get("total_record_count").toString()),
                                Long.parseLong(record.get("start_time").toString()),
                                Long.parseLong(record.get("end_time").toString()));

                        windowAggregatedResultService.insertRecord(windowAggregatedResultDTO);
                    }
                }  catch (Exception e) {
                    // Handle exceptions during insertion
                    e.printStackTrace();
                }
            }
        });
        kafkaConsumerThread.start();
        dbInsertionThread.start();
    }
    private KafkaConsumer<String, GenericRecord> createConsumer() {
        return new KafkaConsumer<>(this.properties);
    }

    public GenericRecord getLatestRecord() {
        return latestRecord;
    }
    @PreDestroy
    public void stop() {
        this.running = false;
        kafkaConsumerThread.interrupt();
       // dbInsertionThread.interrupt();
    }
}
