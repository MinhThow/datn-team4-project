package com.java6.datn.Controller;



import com.java6.datn.Service.MomoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pay")
public class MomoController {

    private final MomoService momoService;

    public MomoController(MomoService momoService) {
        this.momoService = momoService;
    }

    @GetMapping("/momo")
    public ResponseEntity<Map<String, String>> payWithMomo(
            @RequestParam String orderId,
            @RequestParam Long amount) {
        try {
            String payUrl = momoService.createPayment(orderId, amount);

            Map<String, String> result = new HashMap<>();
            result.put("payUrl", payUrl);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    // ✅ callback sau khi thanh toán (redirect)
    @GetMapping("/momo-return")
    public String returnUrl(@RequestParam Map<String, String> params) {
        return "Kết quả thanh toán MoMo: " + params.toString();
    }

    // ✅ callback IPN từ MoMo
    @PostMapping("/momo-ipn")
    public ResponseEntity<String> ipnUrl(@RequestBody Map<String, Object> body) {
        System.out.println("MoMo IPN Callback: " + body);
        return ResponseEntity.ok("OK");
    }


}
