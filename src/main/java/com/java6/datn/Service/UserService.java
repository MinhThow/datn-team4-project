package com.java6.datn.Service;

import com.java6.datn.DTO.UserDTO;
import com.java6.datn.Entity.User;
import com.java6.datn.DTO.UserCreateDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Integer id);
    UserDTO createUser(UserCreateDTO dto); // nhận mật khẩu
    UserDTO updateUser(Integer id, UserDTO dto); // không cho update mật khẩu
    void deleteUser(Integer id);
    boolean checkPassword(User user, String rawPassword);
    void updatePassword(Integer userId, String newPassword);
}
