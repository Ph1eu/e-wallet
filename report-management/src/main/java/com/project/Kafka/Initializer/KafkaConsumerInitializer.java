package com.project.Kafka.Initializer;

import com.project.Kafka.Consumer.AggregatedTransactionConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerInitializer implements ApplicationRunner {

    @Autowired
    AggregatedTransactionConsumer aggregatedTransactionConsumer;

    @Override
    public void run(ApplicationArguments args) {
        aggregatedTransactionConsumer.init();
    }
}