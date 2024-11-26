package com.example.DA.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Posts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends DateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    private String postTitle;

    @Lob  // This annotation specifies that the field should be treated as a large object (TEXT)
    private String postContent;

    private Integer charged = 0;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Pattern(regexp = "pending|approved|expired|rejected", message = "status must be 'pending', 'approved', 'rejected', or 'expired'")
    @Column(name = "status", nullable = false)
    private String status = "pending";  // default value

    // Restrict postType to 'for_sale' or 'for_rent'
    @Pattern(regexp = "for_sale|for_rent", message = "postType must be 'for_sale' or 'for_rent'")
    @Column(name = "post_type", nullable = false)
    private String postType;

    @Column(name = "payment_status")
    private Integer paymentStatus = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long price;

    // Phương thức kiểm tra xem post đã hết hạn chưa
    public void checkExpiration() {
        if (this.getCreatedAt().plusDays(30).isBefore(LocalDateTime.now())) {
            this.status = "expired"; // Đặt trạng thái thành expired nếu đã qua 30 ngày
        }
    }
}
