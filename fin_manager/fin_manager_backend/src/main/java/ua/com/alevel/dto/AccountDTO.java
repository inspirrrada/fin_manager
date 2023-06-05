package ua.com.alevel.dto;

import lombok.Getter;
import lombok.Setter;
import ua.com.alevel.persistence.entity.Account;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountDTO {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
    }
}
