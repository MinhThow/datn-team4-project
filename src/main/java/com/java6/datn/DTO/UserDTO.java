package com.java6.datn.DTO;


import lombok.Data;

@Data
public class UserDTO {
    private Long userID;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String createdAt; // format ngày thành String nếu muốn
}
// Tạo dto không có trường password để bảo mật