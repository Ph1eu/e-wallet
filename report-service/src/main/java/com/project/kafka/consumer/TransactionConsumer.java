package com.project.kafka.consumer;

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
public class TransactionConsumer {
    private final Properties properties;
    private final String topic; // The Kafka topic to which messages will be sent

    @Autowired
    public TransactionConsumer(@Qualifier("kafkaTransactionConsumerProp")Properties properties,
                               @Value("${transaction.topic}") String topic) {
        this.properties = properties;
        this.topic = topic;

    }
    public void listen(Properties properties){
        try (KafkaConsumer<String, GenericRecord> kafkaConsumer = new KafkaConsumer<>(properties)) {
            kafkaConsumer.subscribe(Collections.singletonList(this.topic));
            while (true) {
                ConsumerRecords<String, GenericRecord> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
                consumerRecords.forEach(record -> System.out.println("Record.value = " + record.value() + " ,Partition=" + record.partition()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
