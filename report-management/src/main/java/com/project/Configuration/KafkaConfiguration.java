package com.project.Configuration;

import com.project.Model.TransactionHistory;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.apache.kafka.streams.StreamsConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;


import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;


@Configuration
public class KafkaConfiguration {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Properties kafkaTransactionProducerProp() {

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://localhost:8090");
     //   properties.setProperty(ProducerConfig.ACKS_CONFIG, "0");//It can be 1 or all
     //   properties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, "1048576");
     //   properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");//It can be none or snappy
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, "2");
        properties.setProperty(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "1000");

        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "1024654");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "200");
        properties.setProperty(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "200");

        return properties;
    }
    @Bean
    public Properties kafkaTransactionConsumerProp() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,  KafkaAvroDeserializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,  KafkaAvroDeserializer.class.getName());
        properties.setProperty("specific.avro.reader", "true");//Activate Avro to use Specific Type Serializer not Generic Type
        properties.setProperty("schema.registry.url", "http://localhost:8090");

        return properties;

    }
    @Bean
    public Properties kafkaOneSecAggregationProp(){
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID().toString());
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8090");
     //   properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);

        return properties;
    }
}
