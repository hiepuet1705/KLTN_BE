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

    public String getAveragePriceByProvince(String province, Integer month, Integer year, String purpose) {
        List<Post> posts;
        if (province == null || province.isEmpty() || province.equalsIgnoreCase("all")) {
            // Lấy tất cả bài đăng đã được phê duyệt cho mục đích được chỉ định
            posts = postRepository.findByStatusAndPurpose("approved", purpose);
        } else {
            // Tìm ID tỉnh dựa vào tên
            Integer provinceId = provinceRepository.findByName(province).get(0).getId();
            posts = postRepository.findPostsByProvinceAndPurpose(provinceId, "approved", purpose);
        }

        // Lọc theo tháng và năm, sau đó tính giá trung bình
        Double averagePrice = posts.stream()
                .filter(post -> {
                    LocalDateTime createdAt = post.getProperty().getCreatedAt();
                    return createdAt.getMonthValue() == month && createdAt.getYear() == year;
                })
                .mapToDouble(Post::getPrice)
                .average()
                .orElse(0.0); // Trả về 0.0 nếu không có bài đăng nào phù hợp

        if ("for_rent".equals(purpose)) {
            double averagePriceInMillions = averagePrice / 1_000_000;
            return String.format("%.2f triệu", averagePriceInMillions);
        } else { // purpose là "for_sale" hoặc bất kỳ giá trị nào khác
            double averagePriceInBillions = averagePrice / 1_000_000_000;
            return String.format("%.2f tỷ", averagePriceInBillions);
        }
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
