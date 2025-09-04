package ru.shop.analytics.service;

import ru.shop.analytics.dto.ProductDto;

public interface HadoopService {

    void saveProducts(ProductDto data) throws Exception;

}
