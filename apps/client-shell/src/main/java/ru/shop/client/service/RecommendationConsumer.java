package ru.shop.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import ru.shop.client.dto.ProductDto;
import ru.shop.client.models.RecommendationEntity;
import ru.shop.client.repository.RecommendationRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Slf4j
public class RecommendationConsumer {

    private final RecommendationRepository repository;

    private final ObjectMapper mapper;

    public RecommendationConsumer(RecommendationRepository repository) {
        this.repository = repository;
        this.mapper = new ObjectMapper();
    }

    @KafkaListener(topics = "${topics.recommendations}", containerFactory = "factoryB")
    public void onRecommendations(ProductDto dto, Acknowledgment acknowledgment) {
        try {
            final RecommendationEntity recommendation = new RecommendationEntity();
            recommendation.setClientId(UUID.randomUUID().toString());
            recommendation.setItemsJson(mapper.writeValueAsString(dto));
            recommendation.setTs(OffsetDateTime.now());
            repository.save(recommendation);
            log.info("Сохранена рекомендация");
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error("Error give recommendation", ex);
        }
    }

}
