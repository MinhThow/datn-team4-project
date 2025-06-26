package com.java6.datn.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    private BigDecimal total;

    @Column(length = 20)
    private String status; // "Chờ xác nhận", ...

    private LocalDateTime orderDate = LocalDateTime.now();

    @Lob
    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "PaymentMethodID")
    private PaymentMethod paymentMethod;
}

