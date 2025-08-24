package com.java6.datn.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendOtpEmail(String to, String otp) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, "utf-8");

            // Nội dung HTML đơn giản
            String html = """
                <div style="font-family: Arial, sans-serif; max-width: 500px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 8px;">
                    <h2 style="color: #333; text-align: center;">Mã xác minh OTP</h2>
                    <p style="text-align: center; font-size: 16px; color: #555;">Mã OTP của bạn (hết hạn sau 5 phút):</p>
                    <div style="text-align: center; margin: 20px 0;">
                        <span style="font-size: 28px; font-weight: bold; letter-spacing: 4px; background: #f3f3f3; padding: 10px 20px; border-radius: 6px; display: inline-block;">
                            """ + otp + """
                        </span>
                    </div>
                    <div style="text-align: center;">
                        <a href="#" style="display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; border-radius: 4px; text-decoration: none; font-weight: bold;">
                            📋 Sao chép mã
                        </a>
                    </div>
                </div>
            """;

            helper.setText(html, true); // true = gửi HTML
            helper.setTo(to);
            helper.setSubject("Mã xác minh OTP");
            mailSender.send(mime);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi gửi email: " + e.getMessage());
        }
    }

}

