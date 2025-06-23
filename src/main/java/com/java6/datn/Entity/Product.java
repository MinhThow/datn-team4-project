package com.java6.datn.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    @Column(nullable = false, length = 150)
    private String name;

    @Lob
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private Integer stock;

    private String image;

    private String size;

    @ManyToOne
    @JoinColumn(name = "CategoryID")
    private Category category;
}


