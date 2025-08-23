package com.java6.datn.Controller;

import com.java6.datn.Service.VnPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pay")
public class VnPayController {

    private final VnPayService vnPayService;

    public VnPayController(VnPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    // API frontend gọi: /api/pay/vnpay?orderId=35&amount=259000
    @GetMapping("/vnpay")
    public ResponseEntity<?> createPayment(
            @RequestParam("orderId") int orderId,
            @RequestParam("amount") long amount,
            HttpServletRequest request
    ) {
        try {
            String ipAddress = request.getRemoteAddr();
            log.info("👉 Tạo thanh toán VNPAY: orderId={}, amount={}, ip={}", orderId, amount, ipAddress);

            String paymentUrl = vnPayService.createPaymentUrl(orderId, amount, ipAddress);

            log.info("✅ PaymentUrl: {}", paymentUrl);
            return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));

        } catch (Exception e) {
            log.error("❌ Lỗi tạo payment VNPAY", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> handleVnpayReturn(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> fields = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : fields.entrySet()) {
            params.put(entry.getKey(), entry.getValue()[0]);
        }

        // Chỉ check mã phản hồi
        String responseCode = params.get("vnp_ResponseCode");
        String message;

        if ("00".equals(responseCode)) {
            message = "Thanh toán thành công";
        } else {
            message = "Thanh toán thất bại. Mã lỗi: " + responseCode;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("message", message);
        result.put("params", params);

        return ResponseEntity.ok(result);
    }

}
