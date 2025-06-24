package com.java6.datn.Service;

import com.java6.datn.DTO.UserDTO;
import com.java6.datn.DTO.UserCreateDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO createUser(UserCreateDTO dto); // nhận mật khẩu
    UserDTO updateUser(Long id, UserDTO dto); // không cho update mật khẩu
    void deleteUser(Long id);
}
