package com.java6.datn.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ProductImages")
@Data
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageID;

    private Integer productID;
    private String imageUrl;
    private boolean isMain;
}