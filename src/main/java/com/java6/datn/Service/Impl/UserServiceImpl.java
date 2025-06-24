package com.java6.datn.Service.Impl;

import com.java6.datn.DTO.UserCreateDTO;
import com.java6.datn.DTO.UserDTO;
import com.java6.datn.Entity.User;
import com.java6.datn.Mapper.UserMapper;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return UserMapper.toDTO(
                userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"))
        );
    }

    @Override
    public UserDTO createUser(UserCreateDTO dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword()); // TODO: mã hóa mật khẩu
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setRole(dto.getRole() != null ? dto.getRole() : "customer");
        return UserMapper.toDTO(userRepository.save(entity));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO dto) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setRole(dto.getRole());
        return UserMapper.toDTO(userRepository.save(entity));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

