package com.example.DA.service.imp;

import com.example.DA.model.Post;
import com.example.DA.model.Property;
import com.example.DA.repo.PostRepository;
import com.example.DA.repo.PropertyRepository;
import com.example.DA.repo.ProvinceRepository;
import com.example.DA.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class StatisticServiceImp implements StatisticService {

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private PostRepository postRepository;

    public String getAveragePriceByProvince(String province, Integer month, Integer year) {
        List<Property> properties;

        if (province == null || province.isEmpty() || province.equalsIgnoreCase("all")) {
            properties = propertyRepository.findByStatus("approved");
        } else {
            Integer provinceId = provinceRepository.findByName(province).get(0).getId();
            properties = propertyRepository.findPropertiesByProvince(provinceId);
        }

        // Filter by month and year, then calculate the average price
        Double averagePrice = properties.stream()
                .filter(property -> {
                    LocalDateTime createdAt = property.getCreatedAt();
                    return createdAt.getMonthValue() == month && createdAt.getYear() == year;
                })
                .mapToDouble(Property::getPrice)
                .average()
                .orElse(0.0); // Return 0.0 if there are no properties matching the filters

        // Convert to billions and format the result
        double averagePriceInBillions = averagePrice / 1_000_000_000;
        return String.format("%.2f tỷ", averagePriceInBillions);
    }

    @Override
    public Double getTotalRevenue(Integer month, Integer year) {
        List<Post> posts = postRepository.findByStatus("approved");

        // Filter the posts by charge and payment status
        double totalRevenue = posts.stream()
                .filter(post -> post.getCharged() == 1 && post.getPaymentStatus() == 1)
                .filter(post -> {
                    LocalDateTime createdAt = post.getCreatedAt(); // Assuming you have this method
                    return createdAt.getMonthValue() == month && createdAt.getYear() == year;
                })
                .mapToDouble(post -> 10000) // Assuming each valid post contributes 10,000 to revenue
                .sum();

        return totalRevenue;
    }

}
