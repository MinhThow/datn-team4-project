package com.java6.datn.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java6.datn.Config.MomoConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class MomoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createPayment(String orderId, Long amount) throws Exception {
        String requestId = String.valueOf(System.currentTimeMillis());
        String momoOrderId = orderId + "_" + requestId; // orderId duy nhất cho MoMo
        String orderInfo = "Thanh toán đơn hàng " + orderId;

        // ✅ Fix: orderId phải dùng momoOrderId
        String rawHash = "accessKey=" + MomoConfig.ACCESS_KEY +
                "&amount=" + amount +
                "&extraData=" +
                "&ipnUrl=" + MomoConfig.IPN_URL +
                "&orderId=" + momoOrderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + MomoConfig.PARTNER_CODE +
                "&redirectUrl=" + MomoConfig.REDIRECT_URL +
                "&requestId=" + requestId +
                "&requestType=captureWallet";

        String signature = hmacSHA256(rawHash, MomoConfig.SECRET_KEY);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("partnerCode", MomoConfig.PARTNER_CODE);
        body.put("partnerName", "MoMo Payment"); // chỉ gửi trong body, KHÔNG ký
        body.put("storeId", "MomoTestStore");
        body.put("requestId", requestId);
        body.put("amount", amount);
        body.put("orderId", momoOrderId); // ✅ khớp với rawHash
        body.put("orderInfo", orderInfo);
        body.put("redirectUrl", MomoConfig.REDIRECT_URL);
        body.put("ipnUrl", MomoConfig.IPN_URL);
        body.put("lang", "vi");
        body.put("requestType", "captureWallet");
        body.put("extraData", "");
        body.put("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println("🔎 Request gửi lên MoMo: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                MomoConfig.ENDPOINT,
                HttpMethod.POST,
                entity,
                Map.class
        );

        System.out.println("🔎 Phản hồi từ MoMo: " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object payUrl = response.getBody().get("payUrl");
            if (payUrl != null) {
                return payUrl.toString();
            } else {
                return "ERROR: " + response.getBody().get("message");
            }
        }
        return null;
    }

    private String hmacSHA256(String data, String key) throws Exception {
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSha256.init(secretKeySpec);
        byte[] bytes = hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }
}
