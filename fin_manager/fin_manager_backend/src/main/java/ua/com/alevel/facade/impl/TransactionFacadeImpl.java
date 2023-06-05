package ua.com.alevel.facade.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ua.com.alevel.dto.AccountStatementDTO;
import ua.com.alevel.dto.TransactionDTO;
import ua.com.alevel.dto.TransactionFormDTO;
import ua.com.alevel.facade.TransactionFacade;
import ua.com.alevel.persistence.entity.Account;
import ua.com.alevel.persistence.entity.Transaction;
import ua.com.alevel.persistence.entity.TransactionRegister;
import ua.com.alevel.persistence.entity.User;
import ua.com.alevel.service.AccountService;
import ua.com.alevel.service.TransactionService;
import ua.com.alevel.service.UserService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final UserService userService;

    @Override
    public List<TransactionDTO> findAll() {
        Collection<Transaction> transactions = transactionService.findAll();
        if (CollectionUtils.isNotEmpty(transactions)) {
            return transactions.stream().map(TransactionDTO::new).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public TransactionDTO findById(Long id) {
        return new TransactionDTO(transactionService.findById(id));
    }

    @Override
    public void create(TransactionFormDTO dto) {
        Transaction transaction = new Transaction();
        String fromAccountNumber = dto.getFromAccountNumber();
        String toAccountNumber = dto.getToAccountNumber();
        Account accountFrom = accountService.findByAccountNumber(fromAccountNumber);
        Account accountTo = accountService.findByAccountNumber(toAccountNumber);
        transaction.setSum(dto.getSum());
        transaction.setFromAccount(accountFrom);
        transaction.setToAccount(accountTo);
        transactionService.create(transaction);
    }

    @Override
    public List<AccountStatementDTO> getAccountStatement(Timestamp startDate, Timestamp endDate, Long accountId) {
        List<AccountStatementDTO> list = new ArrayList<>();
        Long userId = accountService.findUserIdByAccountId(accountId);
        User user = userService.findById(userId);
        Collection<Transaction> accountTransactions = transactionService.findAllByAccountId(startDate, endDate, accountId);
        for (Transaction accountTransaction : accountTransactions) {
            TransactionRegister record = transactionService.findRecordByTransactionIdAndUserId(accountTransaction.getId(), userId).stream().toList().get(0);
            AccountStatementDTO accountStatementDTO = new AccountStatementDTO(user, accountTransaction, record);
            list.add(accountStatementDTO);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            return list;
        }
        return Collections.emptyList();
    }
}
