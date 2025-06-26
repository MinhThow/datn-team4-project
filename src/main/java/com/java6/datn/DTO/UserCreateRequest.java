package com.java6.datn.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserCreateRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @Size(min = 6, max = 50)
    @NotBlank
    private String password;

    @Pattern(regexp="\\d{10,20}") // xác thực giá trị chuỗi, trong chuỗi phải ć ít nhất 10 và tối a 20 chữ số
    private String phone;

    private String address;
}

