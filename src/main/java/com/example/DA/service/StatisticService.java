package com.example.DA.service;

public interface StatisticService {
    public String getAveragePriceByProvince(String province, Integer month, Integer year, String purpose);

    public Double getTotalRevenue(Integer month, Integer year);
}
