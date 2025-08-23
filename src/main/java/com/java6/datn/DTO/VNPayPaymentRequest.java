package com.java6.datn.DTO;



import lombok.Data;

@Data
public class VNPayPaymentRequest {
    private int orderId;   // ID đơn hàng
    private Long amount;    // số tiền
    private String bankCode; // optional (nếu muốn chỉ định ngân hàng)
}
