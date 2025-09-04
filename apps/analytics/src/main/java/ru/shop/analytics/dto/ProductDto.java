package ru.shop.analytics.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {

    @JsonProperty("product_id")
    private String productId;
    private String name;
    private String description;
    private PriceDto price;
    private String category;
    private String brand;
    private StockDto stock;
    private String sku;
    private List<String> tags;
    private List<ImageUrlDto> images;
    private SpecificationsDto specifications;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    private String index;
    @JsonProperty("store_id")
    private String storeId;

}
