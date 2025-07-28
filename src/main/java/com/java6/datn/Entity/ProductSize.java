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

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;
    private String size;
    @Column(name = "Stock")
    private Integer stock;

}
