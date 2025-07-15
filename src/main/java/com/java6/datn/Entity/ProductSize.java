package com.java6.datn.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ProductSizes")
@Data
public class ProductSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productSizeID;

    private Integer productID;
    private String size;
    private Integer stock;
}
