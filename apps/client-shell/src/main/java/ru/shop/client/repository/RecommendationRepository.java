package ru.shop.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shop.client.models.RecommendationEntity;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<RecommendationEntity, Long> {

    List<RecommendationEntity> findTopByOrderByTsDesc();

}
