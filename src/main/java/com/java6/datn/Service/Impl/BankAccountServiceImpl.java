package com.java6.datn.Service.Impl;

import com.java6.datn.Entity.BankAccount;
import com.java6.datn.Entity.User;
import com.java6.datn.Repository.BankAccountRepository;
import com.java6.datn.Service.BankAccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository repository;

    public BankAccountServiceImpl(BankAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BankAccount> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public BankAccount save(BankAccount account) {
        return repository.save(account);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
