package com.java6.datn.DTO;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String role;
}
