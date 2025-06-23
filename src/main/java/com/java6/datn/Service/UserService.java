package com.java6.datn.Service;

import com.java6.datn.DTO.UserCreateRequest;
import com.java6.datn.DTO.UserDTO;
import com.java6.datn.Entity.User;
import com.java6.datn.Mapper.UserMapper;
import com.java6.datn.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO create(UserCreateRequest req) {
        User entity = UserMapper.toEntity(req);
        // Mã hóa mật khẩu
        User saved = userRepository.save(entity);
        return UserMapper.toDTO(saved);
    }

    public UserDTO getById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng (User)"));
    }
}
