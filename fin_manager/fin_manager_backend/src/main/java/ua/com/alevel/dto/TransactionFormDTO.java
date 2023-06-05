package ua.com.alevel.dto;

import lombok.Getter;
import lombok.Setter;
import ua.com.alevel.persistence.entity.Transaction;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionFormDTO {

    private BigDecimal sum;
    private String fromAccountNumber;
    private String toAccountNumber;

    public TransactionFormDTO() {
    }

    public TransactionFormDTO(Transaction transaction) {
        this.sum = transaction.getSum();
        this.fromAccountNumber = transaction.getFromAccount().getAccountNumber();
        this.toAccountNumber = transaction.getToAccount().getAccountNumber();
    }
}
