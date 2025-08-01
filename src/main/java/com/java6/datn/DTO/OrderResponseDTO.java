package com.java6.datn.DTO;

//Dữ liệu trả về cho fe
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Integer orderId;
    private String recipientName;
    private String phone;
    private String shippingAddress;
    private String note;
    private BigDecimal total;
    private String status;
    private LocalDateTime orderDate;
    private String paymentMethodName;
    private List<OrderItemDTO> orderItems;
    private String name; // tên người dùng
}

