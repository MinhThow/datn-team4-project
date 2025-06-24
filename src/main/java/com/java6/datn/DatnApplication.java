package com.java6.datn;

import com.java6.datn.Entity.Category;
import com.java6.datn.Repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


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
