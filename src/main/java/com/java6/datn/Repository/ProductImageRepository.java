package com.java6.datn.Repository;

import com.java6.datn.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    ProductImage findFirstByProductIDAndIsMain(Integer productId, boolean isMain);
}
