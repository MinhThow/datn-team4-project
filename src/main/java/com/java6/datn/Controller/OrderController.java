package com.java6.datn.controller;

import com.java6.datn.DTO.OrderRequestDTO;
import com.java6.datn.DTO.OrderResponseDTO;
import com.java6.datn.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {

        System.out.println("📦 USER ID: " + orderRequestDTO.getUserId());
        System.out.println("💳 PAYMENT METHOD ID: " + orderRequestDTO.getPaymentMethodId());

        orderService.createOrder(orderRequestDTO);
        return ResponseEntity.ok("Đặt hàng thành công");
    }
}


