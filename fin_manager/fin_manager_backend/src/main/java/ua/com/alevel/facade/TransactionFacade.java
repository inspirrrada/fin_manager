package ua.com.alevel.facade;

import ua.com.alevel.dto.AccountStatementDTO;
import ua.com.alevel.dto.TransactionDTO;
import ua.com.alevel.dto.TransactionFormDTO;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionFacade {

    List<TransactionDTO> findAll();

    TransactionDTO findById(Long id);

    void create(TransactionFormDTO dto);

    List<AccountStatementDTO> getAccountStatement(Timestamp startDate, Timestamp endDate, Long accountId);
}
