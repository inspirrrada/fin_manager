package ua.com.alevel.service;

import ua.com.alevel.persistence.entity.Transaction;
import ua.com.alevel.persistence.entity.TransactionRegister;

import java.sql.Timestamp;
import java.util.Collection;

public interface TransactionService {

    void create(Transaction transaction);

    Transaction findById(Long id);

    Collection<Transaction> findAll();

    Collection<Transaction> findAllByAccountId(Timestamp startDate, Timestamp endDate, Long accountId);

    Collection<TransactionRegister> findRecordByTransactionIdAndUserId(Long transactionId, Long userId);
}
