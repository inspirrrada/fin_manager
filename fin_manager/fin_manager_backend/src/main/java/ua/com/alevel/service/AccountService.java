package ua.com.alevel.service;

import ua.com.alevel.persistence.entity.Account;

import java.util.Collection;


public interface AccountService {

    void create(Account account);

    void update(Account account);

    Account findById(Long id);

    Collection<Account> findAll();

    Account findByAccountNumber(String accountNumber);

    Long findUserIdByAccountId(Long id);
}
