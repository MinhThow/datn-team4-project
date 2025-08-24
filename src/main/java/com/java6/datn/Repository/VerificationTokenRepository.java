package com.java6.datn.Repository;

import com.java6.datn.Entity.User;
import com.java6.datn.Entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    // Tìm OTP theo user và mã OTP
    Optional<VerificationToken> findByUserAndOtp(User user, String otp);

    // Xóa tất cả OTP của user (khi gửi mã mới)
    void deleteByUser(User user);
}
