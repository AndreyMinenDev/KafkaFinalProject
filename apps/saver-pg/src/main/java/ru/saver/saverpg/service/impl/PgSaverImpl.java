package ru.saver.saverpg.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.saver.saverpg.models.ProductEntity;
import ru.saver.saverpg.repository.ProductRepository;
import ru.saver.saverpg.service.PgSaver;

import java.math.BigDecimal;

@Service
@Slf4j
public class PgSaverImpl implements PgSaver {

    private final ProductRepository repository;
    private final ObjectMapper mapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final String errorTopic;

    public PgSaverImpl(ProductRepository repository,
                       KafkaTemplate<String, String> kafkaTemplate,
                       @Value("${topics.error}") String errorTopic) {
        this.repository = repository;
        this.mapper = new ObjectMapper();
        this.kafkaTemplate = kafkaTemplate;
        this.errorTopic = errorTopic;
    }

    @Override
    public void saveJson(String json) {
        try {
            final JsonNode node = mapper.readTree(json);
            final ProductEntity product = new ProductEntity();
            product.setSku(node.hasNonNull("sku") ? node.get("sku").asText() : null);
            if (product.getSku() == null) return;
            product.setProductId(node.hasNonNull("product_id") ? node.get("product_id").asText() : null);
            product.setName(node.hasNonNull("name") ? node.get("name").asText() : null);
            product.setCategory(node.hasNonNull("category") ? node.get("category").asText() : null);
            product.setBrand(node.hasNonNull("brand") ? node.get("brand").asText() : null);
            if (node.has("price") && node.get("price").has("amount")) {
                product.setPriceAmount(new BigDecimal(node.get("price").get("amount").asText()));
                if (node.get("price").has("currency")) product.setPriceCurrency(node.get("price").get("currency").asText());
            }
            repository.save(product);
        } catch (Exception ex) {
            kafkaTemplate.send(errorTopic, json);
            log.error("Save product:", ex);
        }
    }
}
