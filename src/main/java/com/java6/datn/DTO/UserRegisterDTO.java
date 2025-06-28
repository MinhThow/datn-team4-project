package com.java6.datn.DTO;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String email;
    private String username;
    private String password;
    private String confirmPassword;
}
