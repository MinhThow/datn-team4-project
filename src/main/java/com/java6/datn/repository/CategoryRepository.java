package com.java6.datn.repository;

import com.java6.datn.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Nếu cần thêm query thì khai báo thêm
}
