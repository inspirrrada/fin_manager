package ua.com.alevel.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transactions_register")
public class TransactionRegister extends BaseEntity {

    @ManyToOne
    private Transaction transaction;

    @ManyToOne
    private TransactionCategory transactionCategory;

    @ManyToOne
    private User user;

    public TransactionRegister() {
        super();
    }
}
