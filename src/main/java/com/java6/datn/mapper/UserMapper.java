package com.java6.datn.mapper;

import com.java6.datn.dto.UserDTO;
import com.java6.datn.entity.User;

import java.time.format.DateTimeFormatter;

public class UserMapper {
    public static UserDTO toDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setUserID(entity.getUserID());
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
