package com.java6.datn.Controller;

import com.java6.datn.DTO.OrderRequestDTO;
import com.java6.datn.DTO.OrderResponseDTO;
import com.java6.datn.Service.OrderService;
import com.java6.datn.Service.MomoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MomoService momoService;

    // Tạo đơn hàng
//    @PostMapping
//    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
//        orderService.createOrder(orderRequestDTO);
//        return ResponseEntity.ok("Đặt hàng thành công");
//    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        // service trả về OrderResponseDTO
        OrderResponseDTO newOrder = orderService.createOrder(orderRequestDTO);

        // trả về JSON có id + message
        Map<String, Object> response = new HashMap<>();
        response.put("id", newOrder.getOrderId());              // để frontend gọi MoMo
        response.put("message", "Đặt hàng thành công");    // để hiện thông báo

        return ResponseEntity.ok(response);
    }


    // Lấy danh sách order
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Lấy order theo ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Integer orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        if (order == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(order);
    }

    // Cập nhật trạng thái order
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Integer orderId,
                                                              @RequestBody OrderResponseDTO dto) {
        OrderResponseDTO updated = orderService.updateOrderStatus(orderId, dto.getStatus());
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // 👉 Thanh toán qua MoMo (FE truyền amount)
//    @GetMapping("/{orderId}/pay-momo")
//    public ResponseEntity<Void> payWithMomo(@PathVariable Integer orderId,
//                                            @RequestParam Double amount) throws Exception {
//        String payUrl = momoService.createPayment(amount, orderId);
//
//        // Redirect qua MoMo
//        return ResponseEntity.status(302)
//                .header("Location", payUrl)
//                .build();
//    }
}
