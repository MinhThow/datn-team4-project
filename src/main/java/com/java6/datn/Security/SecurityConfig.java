package com.java6.datn.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer; // ✅ Import này


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF
                .csrf(csrf -> csrf.disable())

                // Phân quyền
                .authorizeHttpRequests(auth -> auth
                        // Các endpoint giỏ hàng cần đăng nhập
                        .requestMatchers("/cart/**", "/checkout/**").authenticated()

                        // Các request còn lại public
                        .anyRequest().permitAll()
                )

                // Sử dụng login form tùy chỉnh
                .formLogin(form -> form
                        .loginPage("/login")         // Chỉ định view login của bạn
                        .loginProcessingUrl("/login")// POST xử lý login
                        .defaultSuccessUrl("/", true) // Thành công về trang chủ
                        .permitAll()
                )

                // Nếu không cần http basic
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}






//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // Tắt CSRF
//                .csrf(csrf -> csrf.disable())
//
//                // Quy định phân quyền
//                .authorizeHttpRequests(auth -> auth
//                        // ✅ Các API liên quan giỏ hàng và thanh toán yêu cầu đăng nhập
//                        .requestMatchers("/cart/**", "/checkout/**").authenticated()
//
//                        // ✅ Các request còn lại đều cho phép
//                        .anyRequest().permitAll()
//                )
//
//                // Hiện form login mặc định
//                .formLogin(Customizer.withDefaults())
//
//                // Tắt http basic
//                .httpBasic(httpBasic -> httpBasic.disable());
//
//        return http.build();
//    }
//}

