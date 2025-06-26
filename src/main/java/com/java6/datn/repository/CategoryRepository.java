package com.java6.datn.repository;

import com.java6.datn.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Additional custom query methods can be added here if needed
} 