package com.java6.datn.Security;

import com.java6.datn.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Integer userId;
    private final String email;
    private final String password;
    private final String role;
    private final String fullname;
    private final String phone;
    private final String address;

    public CustomUserDetails(User user) {
        this.userId = user.getUserID();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.fullname = user.getName();   // 👈 thêm dòng này
        this.phone = user.getPhone();         // 👈 thêm dòng này
        this.address = user.getAddress();     // 👈 thêm dòng này
    }

    public Integer getUserId() { return userId; }

    public String getEmail() { return email; }

    public String getFullname() { return fullname; }   // 👈 thêm getter

    public String getPhone() { return phone; }         // 👈 thêm getter

    public String getAddress() { return address; }     // 👈 thêm getter

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String safeRole = (role != null) ? role.toUpperCase() : "USER";
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + safeRole));
    }


    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
