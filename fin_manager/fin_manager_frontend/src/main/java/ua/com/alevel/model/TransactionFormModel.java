package ua.com.alevel.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class TransactionFormModel {

    private BigDecimal sum;
    private String fromAccountNumber;
    private String toAccountNumber;
}
