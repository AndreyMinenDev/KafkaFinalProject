package ru.shop.client.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class ProductEntity {
    @Id
    private String sku;
    private String name;
}
