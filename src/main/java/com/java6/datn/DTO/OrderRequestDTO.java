package com.java6.datn.DTO;

// Dữ liệu từ client gửi lên để tạo đơn hàng

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private Integer userId;
    private String recipientName;
    private String phone;
    private String shippingAddress;
    private String note;
    private Integer paymentMethodId;
    private List<OrderItemDTO> orderItems;
}

