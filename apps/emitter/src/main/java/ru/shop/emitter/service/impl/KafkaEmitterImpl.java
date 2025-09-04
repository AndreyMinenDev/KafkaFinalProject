package ru.shop.emitter.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.shop.emitter.service.KafkaEmitter;
import ru.shop.emitter.service.ProductsService;
import ru.shop.emitter.service.dto.ProductDto;

@Component
@Slf4j
public class KafkaEmitterImpl implements KafkaEmitter {

    private final String topic;
    private final KafkaTemplate<String, ProductDto> producer;
    private final ProductsService productsService;

    private final ObjectMapper mapper;

    public KafkaEmitterImpl(@Value("${products.topic}") String topic,
                            KafkaTemplate<String, ProductDto> producer,
                            ProductsService productsService) {
        this.topic = topic;
        this.producer = producer;
        this.productsService = productsService;
        this.mapper = new ObjectMapper();
    }

    @Scheduled(fixedRateString = "${app.produce_data_schedule}")
    @Override
    public void emmit() {
        productsService.getProducts().forEach(products -> {
            try {
                log.info("Sent product....");
                final ProductDto productDto = mapper.readValue(products, ProductDto.class);
                producer.send(topic, productDto);
                log.info("Success sent product!");
            } catch (JsonProcessingException ex) {
                log.error("Error parsing json:", ex);
            }
        });
    }
}
