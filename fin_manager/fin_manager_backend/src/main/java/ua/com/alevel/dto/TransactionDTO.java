package ua.com.alevel.dto;

import lombok.Getter;
import lombok.Setter;
import ua.com.alevel.persistence.entity.Account;
import ua.com.alevel.persistence.entity.Transaction;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDTO extends Transaction {

    private Long id;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal sum;

    public TransactionDTO() {
    }

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.fromAccount = transaction.getFromAccount();
        this.toAccount = transaction.getToAccount();
        this.sum = transaction.getSum();
    }
}
