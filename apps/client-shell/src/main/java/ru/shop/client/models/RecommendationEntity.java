package ru.shop.client.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(name = "client_recommendations")
@Data
public class RecommendationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientId;
    @Column(columnDefinition = "text")
    private String itemsJson;
    private OffsetDateTime ts;
}
