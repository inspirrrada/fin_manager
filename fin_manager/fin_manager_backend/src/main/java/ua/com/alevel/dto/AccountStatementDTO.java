package ua.com.alevel.dto;

import lombok.Getter;
import lombok.Setter;
import ua.com.alevel.persistence.entity.Transaction;
import ua.com.alevel.persistence.entity.TransactionCategory;
import ua.com.alevel.persistence.entity.TransactionRegister;
import ua.com.alevel.persistence.entity.User;
import ua.com.alevel.persistence.type.TransactionType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class AccountStatementDTO {

    private OffsetDateTime date;
    private TransactionType transactionCategory;
    private String relatedUserFullName;
    private BigDecimal sum;

    public AccountStatementDTO() {
    }

    public AccountStatementDTO(User user, Transaction transaction, TransactionRegister transactionRecord) {
        this.date = transaction.getCreated();
        User otherUser = transaction.getFromAccount().getUser();
        User relatedUser;
        if (!user.equals(otherUser)) {
            relatedUser = otherUser;
        } else {
            relatedUser = transaction.getToAccount().getUser();
        }
        this.relatedUserFullName = relatedUser.getFirstName() + " " + relatedUser.getLastName();
        TransactionCategory category = transactionRecord.getTransactionCategory();
        this.transactionCategory = category.getTransactionType();
        if (!category.getIsIncome()) {
            this.sum = transaction.getSum().negate();
        } else {
            this.sum = transaction.getSum();
        }
    }
}
