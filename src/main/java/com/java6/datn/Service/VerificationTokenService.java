package com.java6.datn.Service;

import com.java6.datn.Entity.User;
import com.java6.datn.Entity.VerificationToken;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Repository.VerificationTokenRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    // Tạo mã OTP mới cho user
    @Transactional
    public String createOtpForUser(User user) {
    	try {
            // Xóa OTP cũ của user
            tokenRepository.deleteByUser(user);

            // Sinh mã OTP ngẫu nhiên 6 số
            String otp = String.format("%06d", new Random().nextInt(999999));

            // Tạo đối tượng VerificationToken
            VerificationToken token = VerificationToken.builder()
                    .otp(otp)
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusMinutes(5)) // hết hạn sau 5 phút
                    .build();

            // Lưu vào DB
            tokenRepository.save(token);

            return otp;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tạo OTP cho user " + user.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể tạo OTP. Vui lòng thử lại.");
        }
    }

    // Xác minh OTP
    public boolean verifyOtp(String otp, User user) {
    	try {
            Optional<VerificationToken> tokenOpt = tokenRepository.findByUserAndOtp(user, otp);

            if (tokenOpt.isEmpty()) {
                return false; // Không tìm thấy OTP
            }

            VerificationToken token = tokenOpt.get();

            // Kiểm tra hết hạn
            if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
                tokenRepository.delete(token); // Xóa OTP hết hạn
                return false;
            }

            // Nếu hợp lệ → Xóa OTP khỏi DB
            tokenRepository.delete(token);

            // Đánh dấu user đã xác minh email
            user.setEmailVerified(true);
            userRepository.save(user);

            return true;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi xác minh OTP cho user " + user.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xác minh OTP. Vui lòng thử lại.");
        }
    }
}
