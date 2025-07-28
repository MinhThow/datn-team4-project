package com.java6.datn.Repository;

import com.java6.datn.Entity.LoginHistory;
import com.java6.datn.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    List<LoginHistory> findTop10ByUserOrderByLoginTimeDesc(User user);
}
