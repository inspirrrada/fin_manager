package ua.com.alevel.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.alevel.exceptions.AccountNotFoundException;
import ua.com.alevel.exceptions.NegativeBalanceAccountException;
import ua.com.alevel.exceptions.NegativeSumOfTransactionException;
import ua.com.alevel.exceptions.TheSameAccountsOfSenderAndReceiverException;
import ua.com.alevel.persistence.entity.*;
import ua.com.alevel.persistence.repository.AccountRepository;
import ua.com.alevel.persistence.repository.TransactionCategoryRepository;
import ua.com.alevel.persistence.repository.TransactionRegisterRepository;
import ua.com.alevel.persistence.repository.TransactionRepository;
import ua.com.alevel.service.AccountService;
import ua.com.alevel.service.TransactionService;
import ua.com.alevel.service.UserService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountService accountService;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionRegisterRepository transactionRegisterRepository;
    private final UserService userService;

    @Override
    public void create(Transaction transaction) {
        transactionRepository.save(transaction);
        BigDecimal transactionSum = transaction.getSum();
        if (transactionSum.compareTo(BigDecimal.ZERO) == 1) {
            Account accountFrom = transaction.getFromAccount();
            Account accountTo = transaction.getToAccount();
            BigDecimal accountFromBalance = accountFrom.getBalance();
            accountFromBalance = accountFromBalance.subtract(transactionSum);
            if (accountFromBalance.compareTo(BigDecimal.ZERO) == 1 || accountFromBalance.compareTo(BigDecimal.ZERO) == 0) {
                if (accountTo != null) {
                    if (!accountFrom.equals(accountTo)) {
                        BigDecimal accountToBalance = accountTo.getBalance();
                        accountToBalance = accountToBalance.add(transactionSum);
                        accountFrom.setBalance(accountFromBalance);
                        accountTo.setBalance(accountToBalance);
                        accountService.update(accountFrom);
                        accountService.update(accountTo);
                        TransactionRegister transactionIncomeRecord = new TransactionRegister();
                        transactionIncomeRecord.setTransaction(transaction);
                        TransactionCategory incomeCategory = transactionCategoryRepository.findById(1L).get();
                        transactionIncomeRecord.setTransactionCategory(incomeCategory);
                        Long userFromId = accountRepository.findUserIdByAccountId(accountFrom.getId());
                        User userFrom = userService.findById(userFromId);
                        transactionIncomeRecord.setUser(userFrom);
                        transactionRegisterRepository.save(transactionIncomeRecord);
                        TransactionRegister transactionExpenseRecord = new TransactionRegister();
                        TransactionCategory expenseCategory = transactionCategoryRepository.findById(2L).get();
                        transactionExpenseRecord.setTransactionCategory(expenseCategory);
                        transactionExpenseRecord.setTransaction(transaction);
                        Long userToId = accountRepository.findUserIdByAccountId(accountTo.getId());
                        User userTo = userService.findById(userToId);
                        transactionExpenseRecord.setUser(userTo);
                        transactionRegisterRepository.save(transactionExpenseRecord);
                    } else {
                        throw new TheSameAccountsOfSenderAndReceiverException("Can't be the same account in one transaction!");
                    }
                } else {
                    throw new AccountNotFoundException("Such account not found!");
                }
            } else {
                throw new NegativeBalanceAccountException("Transfer amount can't be more than balance!");
            }
        } else {
            throw new NegativeSumOfTransactionException("Amount is less or equal 0!");
        }
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).get();
    }

    @Override
    public Collection<Transaction> findAll() {
        return transactionRepository.findAll();
    }


    @Override
    public Collection<Transaction> findAllByAccountId(Timestamp startDate, Timestamp endDate, Long accountId) {
        if (startDate != null && endDate != null) {
            return transactionRepository.findAllByAccountIdBetweenDates(startDate, endDate, accountId);
        } else if (endDate == null && startDate != null) {
            return transactionRepository.findAllByAccountIdFromDate(startDate, accountId);
        } else if (startDate == null && endDate != null ) {
            return transactionRepository.findAllByAccountIdToDate(endDate, accountId);
        } else {
            return transactionRepository.findAllByAccountIdWithoutDates(accountId);
        }
    }

    @Override
    public Collection<TransactionRegister> findRecordByTransactionIdAndUserId(Long transactionId, Long userId) {
        return transactionRegisterRepository.findRecordByTransactionIdAndUserId(transactionId, userId);
    }
}
