package com.java6.datn.repository;

import com.java6.datn.entity.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleDetailRepository extends JpaRepository<SaleDetail, Integer> {
    
    @Query("SELECT sd FROM SaleDetail sd WHERE sd.productID = :productId AND sd.saleID IN " +
           "(SELECT s.saleID FROM Sale s WHERE s.startDate <= CURRENT_TIMESTAMP AND s.endDate >= CURRENT_TIMESTAMP)")
    Optional<SaleDetail> findActiveSaleByProductId(@Param("productId") Integer productId);
    
    @Query("SELECT sd FROM SaleDetail sd WHERE sd.productID IN :productIds AND sd.saleID IN " +
           "(SELECT s.saleID FROM Sale s WHERE s.startDate <= CURRENT_TIMESTAMP AND s.endDate >= CURRENT_TIMESTAMP)")
    List<SaleDetail> findActiveSalesByProductIds(@Param("productIds") List<Integer> productIds);
} 