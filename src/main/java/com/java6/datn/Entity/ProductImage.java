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

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;

    private String imageUrl;
    private boolean isMain;
}