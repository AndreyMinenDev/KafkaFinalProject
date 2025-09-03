package ru.shop.emitter.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.shop.emitter.service.KafkaEmitter;
import ru.shop.emitter.service.ProductsService;

@Component
@Slf4j
public class KafkaEmitterImpl implements KafkaEmitter {

    private final String topic;
    private final KafkaTemplate<String, String> producer;
    private final ProductsService productsService;

    public KafkaEmitterImpl(@Value("${products.topic}") String topic,
                            KafkaTemplate<String, String> producer,
                            ProductsService productsService) {
        this.topic = topic;
        this.producer = producer;
        this.productsService = productsService;
    }

    @Scheduled(fixedRateString = "${app.produce_data_schedule}")
    @Override
    public void emmit() {
        productsService.getProducts().forEach(products -> {
            log.info("Sent product....");
            producer.send(topic, products);
            log.info("Success sent product!");
        });
    }
}
