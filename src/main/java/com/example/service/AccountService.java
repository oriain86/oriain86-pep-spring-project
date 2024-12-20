package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty()
                || account.getPassword() == null || account.getPassword().length() < 4) {
            return Optional.empty();
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            return Optional.empty();
        }
        Account savedAccount = accountRepository.save(account);
        return Optional.of(savedAccount);
    }

    public Optional<Account> loginAccount(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);
        return account.filter(acc -> acc.getPassword().equals(password));
    }
    
    public boolean isValidUser(int userId) {
        return accountRepository.findById(userId).isPresent();
    }
}
