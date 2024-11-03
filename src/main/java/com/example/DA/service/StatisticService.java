package com.example.DA.service;

public interface StatisticService {
    public String getAveragePriceByProvince(String province, Integer month, Integer year);

    public Double getTotalRevenue(Integer month, Integer year);
}
