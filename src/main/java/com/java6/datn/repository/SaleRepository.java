package com.java6.datn.repository;

import com.java6.datn.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    
    @Query("SELECT s FROM Sale s WHERE s.startDate <= CURRENT_TIMESTAMP AND s.endDate >= CURRENT_TIMESTAMP")
    List<Sale> findActiveSales();
} 