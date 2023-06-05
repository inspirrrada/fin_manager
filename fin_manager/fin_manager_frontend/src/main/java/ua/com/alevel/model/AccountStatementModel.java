package ua.com.alevel.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
public class AccountStatementModel {

    private OffsetDateTime date;
    private TransactionType transactionCategory;
    private String relatedUserFullName;
    private BigDecimal sum;
}
