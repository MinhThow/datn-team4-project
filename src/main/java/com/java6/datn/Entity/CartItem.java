package com.java6.datn.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ProductID")
    private Product product;

    private Integer quantity = 1;
    
    @Column(name = "Size")
    private String size; // Added for size selection support

    private LocalDateTime addedAt = LocalDateTime.now();
}

