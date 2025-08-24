package com.java6.datn.Controller;

import com.java6.datn.Service.VnPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // ✅ Sửa ở đây
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller // ✅ Đổi từ @RestController thành @Controller
@RequestMapping("/api/pay")
public class VnPayController {

    private final VnPayService vnPayService;

    public VnPayController(VnPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    // ✅ API tạo thanh toán vẫn trả JSON nên cần @ResponseBody
    @GetMapping("/vnpay")
    @ResponseBody
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

    // ✅ Callback hiển thị trang thành công
    @GetMapping("/vnpay-return")
    public String handleVnpayReturn(HttpServletRequest request, Model model) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));

        String responseCode = params.get("vnp_ResponseCode");
        String message;

        if ("00".equals(responseCode)) {
            message = "Thanh toán thành công";
        } else {
            message = "Thanh toán thất bại. Mã lỗi: " + responseCode;
        }

        model.addAttribute("message", message);
        model.addAttribute("params", params);

        return "payment_success"; // ✅ Render file payment_success.html trong templates/
    }
}
