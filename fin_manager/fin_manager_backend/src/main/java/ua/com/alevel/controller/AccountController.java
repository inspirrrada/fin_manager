package ua.com.alevel.controller;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.alevel.dto.AccountDTO;
import ua.com.alevel.dto.AccountStatementDTO;
import ua.com.alevel.dto.UserAccountsDTO;
import ua.com.alevel.facade.AccountFacade;
import ua.com.alevel.facade.TransactionFacade;

import java.sql.Timestamp;
import java.util.Collection;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountFacade accountFacade;
    private final TransactionFacade transactionFacade;

    @GetMapping
    public ResponseEntity<Collection<AccountDTO>> findAll() {
        return ResponseEntity.ok(accountFacade.findAll());
    }

    //+
    @GetMapping("/{id}/all")
    public ResponseEntity<UserAccountsDTO> findAllAccountsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(accountFacade.findAllAccountsByUserId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accountFacade.findById(id));
    }

    @PostMapping
    public ResponseEntity<Boolean> create(@RequestBody AccountDTO dto) {
        accountFacade.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }

    @GetMapping(value = "/{userId}/{accountId}/statement")
    public ResponseEntity<Collection<AccountStatementDTO>> getAccountStatement(@PathVariable Long accountId, @RequestParam(value = "fromDate", required = false) String fromDate, @RequestParam(value = "toDate", required = false) String toDate, @PathVariable String userId) {
        String dateTimeFrom = "";
        String dateTimeTo = "";
        Timestamp fromDateValue = null;
        Timestamp toDateValue = null;
        if (StringUtils.isNotEmpty(fromDate)) {
            dateTimeFrom = fromDate + " 00:00:00.000000000";
            fromDateValue = Timestamp.valueOf(dateTimeFrom);
        }
        if (StringUtils.isNotEmpty(toDate)) {
            dateTimeTo = toDate + " 23:59:59.999999999";
            toDateValue = Timestamp.valueOf(dateTimeTo);
        }
        return ResponseEntity.ok(transactionFacade.getAccountStatement(fromDateValue, toDateValue, accountId));
    }
}
