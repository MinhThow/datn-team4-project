package com.java6.datn.Controller;

import com.java6.datn.DTO.UserCreateDTO;
import com.java6.datn.DTO.UserDTO;
import com.java6.datn.Security.CustomUserDetails;
import com.java6.datn.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO create(@RequestBody UserCreateDTO dto) {
        return userService.createUser(dto);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Integer id, @RequestBody UserDTO dto) {
        return userService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập");
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("fullname", userDetails.getFullname());
        userInfo.put("email", userDetails.getEmail());
        userInfo.put("phone", userDetails.getPhone());
        userInfo.put("address", userDetails.getAddress());

        return ResponseEntity.ok(userInfo);
    }
    }


