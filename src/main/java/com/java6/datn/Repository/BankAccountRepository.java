package com.java6.datn.Repository;

import com.java6.datn.Entity.BankAccount;
import com.java6.datn.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByUser(User user);
}
