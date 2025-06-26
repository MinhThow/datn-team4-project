package com.java6.datn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewID;

    @ManyToOne
    @JoinColumn(name = "ProductID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    private Integer rating;

    @Lob
    private String comment;

    private LocalDateTime reviewDate = LocalDateTime.now();
}

