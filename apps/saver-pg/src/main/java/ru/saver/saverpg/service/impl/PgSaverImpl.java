package ru.saver.saverpg.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

    public PgSaverImpl(ProductRepository repository) {
        this.repository = repository;
        this.mapper = new ObjectMapper();
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
            log.error("Save product:", ex);
        }
    }
}
