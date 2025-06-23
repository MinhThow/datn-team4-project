package com.java6.datn.Controller;

import com.java6.datn.DTO.UserCreateRequest;
import com.java6.datn.DTO.UserDTO;
import com.java6.datn.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserCreateRequest req) {
        return userService.create(req);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getById(id);
    }
}

