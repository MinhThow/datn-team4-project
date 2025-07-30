package com.java6.datn.Mapper;

import com.java6.datn.DTO.UserDTO;
import com.java6.datn.Entity.User;

import java.time.format.DateTimeFormatter;

public class UserMapper {
    public static UserDTO toDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setUserId(entity.getUserID());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setAddress(entity.getAddress());
        dto.setRole(entity.getRole());
        dto.setCreatedAt(entity.getCreatedAt() != null
                ? entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : null);
        return dto;
    }
}
