package ru.saver.saverpg.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.saver.saverpg.dto.ProductDto;
import ru.saver.saverpg.models.ProductEntity;
import ru.saver.saverpg.repository.ProductRepository;
import ru.saver.saverpg.service.PgSaver;


@Service
@Slf4j
public class PgSaverImpl implements PgSaver {

    private final ProductRepository repository;

    private final KafkaTemplate<String, ProductDto> kafkaTemplate;

    private final String errorTopic;

    public PgSaverImpl(ProductRepository repository,
                       KafkaTemplate<String, ProductDto> kafkaTemplate,
                       @Value("${topics.error}") String errorTopic) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.errorTopic = errorTopic;
    }

    @Override
    public void save(ProductDto dto) {
        try {
            final ProductEntity product = new ProductEntity();
            product.setSku(dto.getSku());
            if (product.getSku() == null) return;
            product.setProductId(dto.getProductId());
            product.setName(dto.getName());
            product.setCategory(dto.getCategory());
            product.setBrand(dto.getBrand());
            product.setPriceAmount(dto.getPrice().getAmount());
            product.setPriceCurrency(dto.getPrice().getCurrency());
            repository.save(product);
        } catch (Exception ex) {
            kafkaTemplate.send(errorTopic, dto);
            log.error("Save product:", ex);
        }
    }
}
