package com.example.DA.service;

import com.example.DA.util.MoMoUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class MoMoPaymentService {

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.redirectUrl}")
    private String redirectUrl;

    @Value("${momo.notifyUrl}")
    private String notifyUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();


    public String createPayment(Integer postId, Long amount) throws Exception {
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = "POST_" + postId + "_" + requestId;
        String orderInfo = "Payment for post ID " + postId;
        String extraData = ""; // Giữ trống nếu không có dữ liệu bổ sung

        // Chuỗi rawData để tạo chữ ký, chú ý thứ tự tham số
        String rawData = "accessKey=" + accessKey
                + "&amount=" + amount
                + "&extraData=" + extraData
                + "&ipnUrl=" + notifyUrl  // Dùng notifyUrl cho ipnUrl
                + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo
                + "&partnerCode=" + partnerCode
                + "&redirectUrl=" + redirectUrl
                + "&requestId=" + requestId
                + "&requestType=captureWallet";

        // Tạo chữ ký với secretKey
        String signature = MoMoUtils.generateSignature(rawData, secretKey);

        // Tạo payload cho MoMo
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", partnerCode);
        requestBody.put("accessKey", accessKey);
        requestBody.put("requestId", requestId);
        requestBody.put("amount", amount);
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", orderInfo);
        requestBody.put("redirectUrl", redirectUrl);
        requestBody.put("ipnUrl", notifyUrl);     // Sử dụng notifyUrl cho ipnUrl
        requestBody.put("requestType", "captureWallet");
        requestBody.put("extraData", extraData);
        requestBody.put("signature", signature);  // Thêm chữ ký vào payload
        // Tạo header với Content-Type là application/json
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String jsonBody = mapper.writeValueAsString(requestBody);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        // Gửi request đến MoMo API
        String momoUrl = "https://test-payment.momo.vn/v2/gateway/api/create";
        ResponseEntity<String> response = restTemplate.exchange(momoUrl, HttpMethod.POST, requestEntity, String.class);

        // Lấy URL thanh toán từ phản hồi của MoMo
        Map<String, Object> responseMap = mapper.readValue(response.getBody(), Map.class);

        if (responseMap.get("payUrl") != null) {
            return (String) responseMap.get("payUrl");
        } else {
            throw new Exception("Failed to create MoMo payment: " + responseMap.get("message"));
        }
    }

}
