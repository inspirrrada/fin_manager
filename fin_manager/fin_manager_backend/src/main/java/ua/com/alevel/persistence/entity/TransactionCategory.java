package ua.com.alevel.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ua.com.alevel.persistence.type.TransactionType;

@Getter
@Setter
@Entity
@Table(name = "transaction_categories")
public class TransactionCategory extends BaseEntity {

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "income", nullable = false)
    private Boolean isIncome;

    public TransactionCategory() {
        super();
    }
}
