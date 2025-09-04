package ru.shop.analytics.service.impl;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import ru.shop.analytics.service.HadoopService;

@Service
public class DataListener {
    private final HadoopService hadoopService;

    public DataListener(HadoopService hadoopService) {
        this.hadoopService = hadoopService;
    }

    @KafkaListener(topics = "${topics.productsFiltered}")
    public void productsListener(String data, Acknowledgment acknowledgment) {
        hadoopService.saveProducts(data);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "${topics.requests}")
    public void requestListener(String data, Acknowledgment acknowledgment) {
        hadoopService.saveRequests(data);
        acknowledgment.acknowledge();
    }

}
