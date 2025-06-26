package com.java6.datn.service;

import com.java6.datn.dto.UserDTO;
import com.java6.datn.dto.UserCreateDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Integer id);
    UserDTO createUser(UserCreateDTO dto); // nhận mật khẩu
    UserDTO updateUser(Integer id, UserDTO dto); // không cho update mật khẩu
    void deleteUser(Integer id);
}
