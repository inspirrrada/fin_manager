package ua.com.alevel.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.alevel.persistence.entity.Account;

import java.util.Collection;

@Repository
public interface AccountRepository extends BaseRepository<Account> {

    Account findByAccountNumber(String accountNumber);

    @Query(value = "from Account where user.id =: userId")
    Collection<Account> findAllByUser(@Param("userId") Long id);

    @Query(value = "select user_id from accounts where id = ?1", nativeQuery = true)
    Long findUserIdByAccountId(Long id);
}
