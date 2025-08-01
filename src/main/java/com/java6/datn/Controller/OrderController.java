package com.java6.datn.Controller;

import com.java6.datn.DTO.OrderRequestDTO;
import com.java6.datn.DTO.OrderResponseDTO;
import com.java6.datn.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Integer orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        if (order == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Integer orderId, @RequestBody OrderResponseDTO dto) {
        OrderResponseDTO updated = orderService.updateOrderStatus(orderId, dto.getStatus());
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }
}


