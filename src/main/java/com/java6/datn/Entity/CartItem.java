package com.java6.datn.Entity;


import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Entity
@Table(name = "CartItems")
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemID;

    private Integer userID;
    private Integer productID;
    private Integer productSizeID;
    private Integer quantity;
    private Timestamp addedAt;
}