package com.java6.datn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PaymentMethods")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentMethodID;

    private String name;
    private String description;

    // Getters and setters
    public Integer getPaymentMethodID() { return paymentMethodID; }
    public void setPaymentMethodID(Integer paymentMethodID) { this.paymentMethodID = paymentMethodID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
} 