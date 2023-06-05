package ua.com.alevel.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(precision = 7, scale = 2)
    private BigDecimal balance;

    @ManyToOne
    private User user;

    @Column(name = "active_status")
    private Boolean isActive;

    public Account() {
        super();
        this.balance = new BigDecimal("00.00");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(getId(), account.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
