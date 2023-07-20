package com.project.Kafka.Producer;

import com.project.Configuration.KafkaConfiguration;
import com.project.Model.TransactionHistory;
import com.project.Payload.DTO.TransactionHistoryDTO;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class TransactionProducer {

    private final Properties properties;
    private final String topic; // The Kafka topic to which messages will be sent

    @Autowired
    public TransactionProducer(@Qualifier("kafkaTransactionProducerProp")Properties properties,
                               @Value("${transaction.topic}") String topic) {
        this.properties = properties;
        this.topic = topic;
        createTopicIfNotExists();

    }
    private void createTopicIfNotExists() {
        try (AdminClient adminClient = AdminClient.create(properties)) {
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
    public void publish( String key, TransactionHistory value) {
        Schema.Parser parser = new Schema.Parser();

        Schema schema = parser.parse("{\n" +
                "    \"type\": \"record\",\n" +
                "    \"name\":\"Transaction\",\n" +
                "    \"fields\":[\n" +
//                "        {\"name\":\"senderid\", \"type\":\"string\"},\n" +
//                "        {\"name\":\"recipientid\", \"type\":\"string\"},\n" +
                "        {\"name\":\"transaction_type\", \"type\":\"string\"},\n" +
                "        {\"name\":\"amount\", \"type\":\"int\"},\n" +
                "        {\"name\":\"transaction_date\", \"type\":\"long\",\"logicalType\": \"date\"}\n" +
                "    ]\n" +
                "}");
        try (KafkaProducer<String, GenericRecord> kafkaProducer = new KafkaProducer<>(this.properties)) {
            GenericRecord transaction = new GenericData.Record(schema);
//            transaction.put("senderid",value.getSender());
//            transaction.put("recipientid",value.getRecipient());
            transaction.put("transaction_type",value.getTransaction_type());
            transaction.put("amount",value.getAmount());
            transaction.put("transaction_date",value.getTransaction_date().getTime());

            ProducerRecord<String, GenericRecord> record = new ProducerRecord<>(this.topic, value.getId(), transaction);
            System.out.println(record);
            RecordMetadata recordMetadata = kafkaProducer.send(record).get();
            System.out.println("TimeStamp:" + recordMetadata.timestamp());
            System.out.println("Partition:" + recordMetadata.partition());
            System.out.println("Offset:" + recordMetadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
