package ua.com.alevel.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.alevel.persistence.entity.Account;
import ua.com.alevel.persistence.repository.AccountRepository;
import ua.com.alevel.service.AccountService;

import java.util.Collection;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public void create(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void update(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).get();
    }

    @Override
    public Collection<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public Long findUserIdByAccountId(Long id) {
        return accountRepository.findUserIdByAccountId(id);
    }
}
