package ru.saver.saverpg.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class ProductEntity {

    @Id
    private String sku;
    private String name;
    private String category;
    private String brand;
    @Column(name = "product_id")
    private String productId;
    @Column(name = "price_amount", precision = 14, scale = 2)
    private BigDecimal priceAmount;
    @Column(name = "price_currency")
    private String priceCurrency;

}
