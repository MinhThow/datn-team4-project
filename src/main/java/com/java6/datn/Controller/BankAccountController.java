package com.java6.datn.Controller;

import com.java6.datn.Entity.BankAccount;
import com.java6.datn.Entity.User;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Service.BankAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/api/bank-accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final UserRepository userRepository;

    public BankAccountController(BankAccountService bankAccountService, UserRepository userRepository) {
        this.bankAccountService = bankAccountService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public String createBankAccount(@ModelAttribute("newBankAccount") BankAccount account, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        account.setUser(user);
        bankAccountService.save(account);

        return "redirect:/account";
    }

    @PostMapping("/delete/{id}")
    public String deleteBankAccount(@PathVariable("id") Long id) {
        bankAccountService.deleteById(id);
        return "redirect:/account#bank"; // 👈 trở lại tab Bank Account
    }


}
