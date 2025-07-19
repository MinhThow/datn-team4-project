package com.java6.datn.Repository;

import com.java6.datn.Entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Integer> {
    @Query("SELECT ps FROM ProductSize ps WHERE ps.product.productID = :productID")
    List<ProductSize> findByProduct_ProductID(Integer productID);
    
}
