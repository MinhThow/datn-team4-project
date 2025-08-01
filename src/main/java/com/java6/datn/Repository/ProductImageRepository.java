package com.java6.datn.Repository;

import com.java6.datn.Entity.Product;
import com.java6.datn.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    ProductImage findFirstByProductAndIsMain(Product product, boolean isMain);
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.productID = :productID")
    List<ProductImage> findByProduct_ProductID(Integer productID);
}
