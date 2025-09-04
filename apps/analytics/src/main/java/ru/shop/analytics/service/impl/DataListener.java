package ru.shop.analytics.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import ru.shop.analytics.dto.ProductDto;
import ru.shop.analytics.service.HadoopService;

@Service
@Slf4j
public class DataListener {
    private final HadoopService hadoopService;

    public DataListener(HadoopService hadoopService) {
        this.hadoopService = hadoopService;
    }

    @KafkaListener(topics = "${topics.productsFiltered}")
    public void productsListener(ProductDto data, Acknowledgment acknowledgment) {
        try {
            hadoopService.saveProducts(data);
            acknowledgment.acknowledge();
        } catch (Exception ex) {
               log.error("Error save to hadoop:", ex);
        }
    }

}
