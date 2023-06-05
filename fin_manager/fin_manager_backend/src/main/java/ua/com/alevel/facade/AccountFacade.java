package ua.com.alevel.facade;

import ua.com.alevel.dto.AccountDTO;
import ua.com.alevel.dto.UserAccountsDTO;

import java.util.List;

public interface AccountFacade {

    List<AccountDTO> findAll();

    AccountDTO findById(Long id);

    UserAccountsDTO findAllAccountsByUserId(Long id);

    AccountDTO findByAccountNumber(String accountNumber);

    void create(AccountDTO dto);

    void update(Long id, AccountDTO dto);
}
