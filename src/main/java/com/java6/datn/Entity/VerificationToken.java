package com.java6.datn.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "VerificationToken")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 6, nullable = false)
    private String otp; // Mã OTP 6 số

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Liên kết với bảng Users

    @Column(name = "expiryDate", nullable = false)
    private LocalDateTime expiryDate; // Thời gian hết hạn OTP
}
