package com.java6.datn.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;

    private String accountNumber;

    private String accountHolder;

    private String branch;

    private String cardType; // Credit | Debit

    private String cvv;

    private String expiryDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
