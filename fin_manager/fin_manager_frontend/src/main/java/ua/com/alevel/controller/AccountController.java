package ua.com.alevel.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.alevel.api.AccountApiService;
import ua.com.alevel.model.AccountModel;
import ua.com.alevel.model.AccountStatementModel;
import ua.com.alevel.model.DateFilters;
import ua.com.alevel.model.UserAccountsModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountApiService accountApiService;

    @GetMapping("/{id}/all")
    public String findUserAccounts(@PathVariable Long id, Model model) {
        Optional<UserAccountsModel> userAccountsModelOptional = accountApiService.findAllAccountsByUserId(id);
        if (userAccountsModelOptional.isPresent()) {
            model.addAttribute("user", userAccountsModelOptional.get());
            return "pages/user_accounts";
        }
        return "pages/404";
    }

    @GetMapping("/{userId}/{accountId}/filters")
    public String showAccountFiltersForm(Model model, @PathVariable String userId, @PathVariable String accountId) {
        model.addAttribute("filters", new DateFilters());
        return "pages/account_filters_form";
    }

    @PostMapping("/{userId}/{accountId}/filters")
    public String getFiltersValue(@ModelAttribute("filters") DateFilters dateFilters, @PathVariable String userId, @PathVariable String accountId) {
        return "pages/account_statement";
    }

    @GetMapping("/{userId}/{accountId}/statement")
    public String getAccountStatement(@PathVariable Long userId, @PathVariable Long accountId, Model model, @ModelAttribute DateFilters dateFilters, @RequestParam(value = "fromDate", required = false) String fromDate, @RequestParam(value = "toDate", required = false) String toDate, HttpSession session) {
        Collection<AccountStatementModel> accountStatementModel = accountApiService.getAccountStatement(userId, accountId, fromDate, toDate);
        model.addAttribute("statement", accountStatementModel); //TODO перевірка чи є Optional
        session.setAttribute("statement", accountStatementModel);
        AccountModel accountModel = accountApiService.findById(accountId).get(); ////TODO перевірка чи є Optional
        model.addAttribute("accountModel", accountModel);
        return "pages/account_statement";
    }

    @GetMapping("/download")
    public void getFile(Model model, HttpSession session, HttpServletResponse response) {
        Collection<AccountStatementModel> accountStatementModel = new ArrayList<>();
        if (session.getAttribute("statement") != null) {
            accountStatementModel = (Collection<AccountStatementModel>) session.getAttribute("statement");
        }

        String filename = "Account_statement.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        StatefulBeanToCsv<AccountStatementModel> writer =
                null;
        try {
            writer = new StatefulBeanToCsvBuilder<AccountStatementModel>
                    (response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(false).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            writer.write((List<AccountStatementModel>) accountStatementModel);
        } catch (CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        } catch (CsvRequiredFieldEmptyException e) {
            throw new RuntimeException(e);
        }
    }
}
