package ru.shop.analytics.service;

public interface HadoopService {

    void saveProducts(String data);
    void saveRequests(String data);

    void recommendation();

}
