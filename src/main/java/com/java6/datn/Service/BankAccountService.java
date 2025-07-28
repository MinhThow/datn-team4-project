package com.java6.datn.Service;

import com.java6.datn.Entity.BankAccount;
import com.java6.datn.Entity.User;

import java.util.List;

public interface BankAccountService {
    List<BankAccount> findByUser(User user);
    BankAccount save(BankAccount account);
    void deleteById(Long id);
}
