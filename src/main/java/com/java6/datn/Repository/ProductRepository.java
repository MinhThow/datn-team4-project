package com.java6.datn.Repository;

import com.java6.datn.Entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    // Cách dùng query method theo quan hệ
    List<Product> findByCategory_CategoryID(Integer categoryID);

    // Hoặc dùng @Query để rõ ràng hơn
    @Query("SELECT p FROM Product p WHERE p.category.categoryID = :categoryID")
    List<Product> findProductsByCategory(@Param("categoryID") Integer categoryID);

    List<Product> findByCategoryCategoryID(Integer categoryId, Sort sort);

}

