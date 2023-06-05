package ua.com.alevel.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.alevel.persistence.entity.TransactionRegister;

import java.util.Collection;

@Repository
public interface TransactionRegisterRepository extends BaseRepository<TransactionRegister> {

    @Query(value = "select * from transactions_register where transaction_id = ?1 and user_id = ?2", nativeQuery = true)
    Collection<TransactionRegister> findRecordByTransactionIdAndUserId(Long transactionId, Long userId);

    @Query(value = "select * from transactions_register where transaction_id = ?1", nativeQuery = true)
    Collection<TransactionRegister> findRecordByTransactionId(Long transactionId);
}
