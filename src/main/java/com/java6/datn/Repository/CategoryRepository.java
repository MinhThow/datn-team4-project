package com.java6.datn.Repository;

import com.java6.datn.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Nếu cần thêm query thì khai báo thêm
}
