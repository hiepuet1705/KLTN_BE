package com.example.DA.controller;


import com.example.DA.service.MoMoPaymentService;
import com.example.DA.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/posts")
public class PostPaymentController {
    @Autowired
    private MoMoPaymentService momoPaymentService;

    @Autowired
    private PostService postService;

    @PutMapping("/{postId}/payments")
    public ResponseEntity<String> createPayment(@PathVariable Integer postId, @RequestParam Long amount) {
        try {
            String payUrl = momoPaymentService.createPayment(postId, amount);
            return ResponseEntity.ok(payUrl); // Trả về URL để người dùng thanh toán
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating payment: " + e.getMessage());
        }
    }

    @PostMapping("/payment/notify")
    public ResponseEntity<String> handleNotify(@RequestBody Map<String, Object> notification) {
        Integer postId = Integer.parseInt(notification.get("orderId").toString().split("_")[1]);
        String resultCode = (String) notification.get("resultCode");

        if ("0".equals(resultCode)) {
            // Cập nhật paymentStatus cho bài viết thành công
            postService.updatePaymentStatus(postId);
            return ResponseEntity.ok("Payment successful");
        } else {
            return ResponseEntity.status(400).body("Payment failed");
        }
    }

    @GetMapping("/payment/result")
    public String handleRedirect(
            @RequestParam("orderId") String orderId,
            @RequestParam("resultCode") String resultCode,
            @RequestParam("message") String message) {

        // Lấy thông tin postId từ orderId (ví dụ: POST_1_123456)
        Integer postId = Integer.parseInt(orderId.split("_")[1]);

        // Kiểm tra trạng thái giao dịch và trả về thông báo
        if ("0".equals(resultCode)) {
            postService.updatePaymentStatus(postId);
            return "Thanh toán thành công cho bài đăng ID: " + postId;
        } else {
            return "Thanh toán thất bại: " + message;
        }
    }
}
