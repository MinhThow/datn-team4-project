package com.java6.datn.Service.Impl;

import com.java6.datn.DTO.UserCreateDTO;
import com.java6.datn.DTO.UserDTO;
import com.java6.datn.Entity.User;
import com.java6.datn.Mapper.UserMapper;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Integer id) {
        return UserMapper.toDTO(
                userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"))
        );
    }

    @Override
    public UserDTO createUser(UserCreateDTO dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(passwordEncoder.encode("123"));
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setRole(dto.getRole() != null ? dto.getRole() : "customer");
        return UserMapper.toDTO(userRepository.save(entity));
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO dto) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            entity.setRole(dto.getRole());
        }

        return UserMapper.toDTO(userRepository.save(entity));
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
    @Override
	public boolean checkPassword(User user, String rawPassword) {
		return passwordEncoder.matches(rawPassword, user.getPassword());
	}

	// ✅ Cập nhật mật khẩu mới
	@Override
	public void updatePassword(Integer userId, String newPassword) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		// Nếu role null thì gán lại mặc định
	    if (user.getRole() == null || user.getRole().isBlank()) {
	        user.setRole("customer"); // hoặc "ROLE_CUSTOMER" tùy hệ thống bạn dùng
	    }
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}
}

