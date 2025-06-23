package com.java6.datn.Mapper;



import com.java6.datn.DTO.UserCreateRequest;
import com.java6.datn.DTO.UserDTO;
import com.java6.datn.Entity.User;
import java.time.format.DateTimeFormatter;

// UserMapper có tác dụng chuyển đổi giữa USer và UserDTO
public class UserMapper {

    public static UserDTO toDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setUserID(entity.getUserID());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setAddress(entity.getAddress());
        dto.setRole(entity.getRole());
        dto.setCreatedAt(
                entity.getCreatedAt() != null
                        ? entity.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : null
        );
        return dto;
    }

    public static User toEntity(UserCreateRequest req) {
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword()); // cần mã hóa password
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());
        user.setRole("customer"); // mặc định
        return user;
    }
}

