package ua.com.alevel.api;

import ua.com.alevel.model.TransactionFormModel;

public interface TransactionApiService {

    String createTransaction(TransactionFormModel transactionForm, Long userId, Long accountId);
}
