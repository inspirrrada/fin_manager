package ua.com.alevel.api;

import ua.com.alevel.model.AccountModel;
import ua.com.alevel.model.AccountStatementModel;
import ua.com.alevel.model.UserAccountsModel;

import java.util.Collection;
import java.util.Optional;

public interface AccountApiService {

    Collection<AccountModel> findAll();

    Optional<AccountModel> findById(Long id);

    Optional<AccountModel> findByAccountNumber(String accountModel);

    Collection<AccountStatementModel> getAccountStatement(Long userId, Long accountId, String fromDate, String toDate);

    Optional<UserAccountsModel> findAllAccountsByUserId(Long id);
}
