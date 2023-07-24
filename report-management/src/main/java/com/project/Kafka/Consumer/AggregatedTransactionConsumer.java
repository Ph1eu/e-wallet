package com.project.Kafka.Consumer;

import jakarta.annotation.PostConstruct;
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

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AggregatedTransactionConsumer {
    private final Properties properties;
    private final String OneSectopic; // The Kafka topic to which messages will be sent
    private final String Globaltopic;
    private  KafkaConsumer<String, GenericRecord> kafkaConsumer;
    private GenericRecord latestRecord;
    private volatile boolean running; // To control the consumer loop


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

        Thread consumerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, GenericRecord> consumerRecords = this.kafkaConsumer.poll(Duration.ofSeconds(5));
                consumerRecords.forEach(record -> {
                    latestRecord = record.value();
                    System.out.println("Record.value = " + record.value() + " ,Partition=" + record.partition());
                });
                this.kafkaConsumer.commitSync();
            }
            this.kafkaConsumer.close();
        });

        consumerThread.start();
    }
    private KafkaConsumer<String, GenericRecord> createConsumer() {
        return new KafkaConsumer<>(this.properties);
    }
    public void listenFromOneSec(){
        try (KafkaConsumer<String, GenericRecord> kafkaConsumer = new KafkaConsumer<>(this.properties)) {
            kafkaConsumer.subscribe(Collections.singletonList(this.OneSectopic));
           System.out.println("Kafka consumer created and subscribed to topic: " + this.OneSectopic);

            while (true) {
                ConsumerRecords<String, GenericRecord> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(30));
                consumerRecords.forEach(record -> {
                        // Update the latestRecord with the latest consumed record
                        latestRecord = record.value();
                        System.out.println("Record.value = " + record.value() + " ,Partition=" + record.partition());
                    });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public GenericRecord getLatestRecord() {
        return latestRecord;
    }
    @PreDestroy
    public void stop() {
        this.running = false;
    }
}
