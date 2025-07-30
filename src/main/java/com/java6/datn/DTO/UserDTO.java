package com.java6.datn.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String createdAt; // trả chuỗi ngày
}

