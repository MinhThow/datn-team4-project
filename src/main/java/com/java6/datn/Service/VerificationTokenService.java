package com.java6.datn.Service;

import com.java6.datn.Entity.User;
import com.java6.datn.Entity.VerificationToken;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationTokenService {

	@Autowired
	private VerificationTokenRepository tokenRepo;
	@Autowired
	private UserRepository userRepository; // ✅ Thêm dòng này

	public String createToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
		tokenRepo.save(verificationToken);
		return token;
	}

	public boolean verifyToken(String token) {
		return tokenRepo.findByToken(token).filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now())).map(t -> {
			User user = t.getUser();
			user.setEmailVerified(true); // 👈 thêm cột này nếu bạn cần
			userRepository.save(user); // bạn gọi nếu muốn lưu
			tokenRepo.delete(t);
			return true;
		}).orElse(false);
	}


	 @Transactional 
	public void deleteByUser(User user) {
		tokenRepo.deleteByUser(user);
	}

}
