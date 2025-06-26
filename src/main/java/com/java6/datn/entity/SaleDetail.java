package com.java6.datn.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SaleDetails")
public class SaleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer saleDetailID;

    private Integer saleID;
    private Integer productID;
    private BigDecimal salePrice;

    // Getters and setters
    public Integer getSaleDetailID() { return saleDetailID; }
    public void setSaleDetailID(Integer saleDetailID) { this.saleDetailID = saleDetailID; }
    public Integer getSaleID() { return saleID; }
    public void setSaleID(Integer saleID) { this.saleID = saleID; }
    public Integer getProductID() { return productID; }
    public void setProductID(Integer productID) { this.productID = productID; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
} 