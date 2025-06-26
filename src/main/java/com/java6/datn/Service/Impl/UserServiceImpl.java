package com.java6.datn.service.impl;

import com.java6.datn.dto.UserCreateDTO;
import com.java6.datn.dto.UserDTO;
import com.java6.datn.entity.User;
import com.java6.datn.mapper.UserMapper;
import com.java6.datn.repository.UserRepository;
import com.java6.datn.service.UserService;
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
//        entity.setPassword(dto.getPassword()); // TODO: mã hóa mật khẩu
        entity.setPassword(passwordEncoder.encode(dto.getPassword())); // mã hóa mật khẩu
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
        entity.setRole(dto.getRole());
        return UserMapper.toDTO(userRepository.save(entity));
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}

