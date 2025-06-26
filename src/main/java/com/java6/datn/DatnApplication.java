package com.java6.datn;

import com.java6.datn.entity.Category;
import com.java6.datn.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DatnApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatnApplication.class, args);
    }


    // Test api
//    @Bean
//    CommandLineRunner init(CategoryRepository categoryRepository) {
//        return args -> {
//            Category cat = new Category();
//            cat.setName("Test Category");
//            cat.setDescription("Danh mục test");
//            categoryRepository.save(cat);
//        };
//    }
}
