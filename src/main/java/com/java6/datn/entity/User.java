package com.java6.datn.entity;

import jakarta.persistence.*;
import jakarta.persistence.Column;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 20)
    private String phone;

    @Lob
    private String address;

    @Column(length = 20)
    private String role; // 'customer' or 'admin'

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt = LocalDateTime.now();
}
