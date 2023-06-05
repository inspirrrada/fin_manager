package ua.com.alevel.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.alevel.exceptions.AccountNotFoundException;
import ua.com.alevel.exceptions.NegativeBalanceAccountException;
import ua.com.alevel.exceptions.NegativeSumOfTransactionException;
import ua.com.alevel.exceptions.TheSameAccountsOfSenderAndReceiverException;
import ua.com.alevel.persistence.entity.*;
import ua.com.alevel.persistence.repository.TransactionCategoryRepository;
import ua.com.alevel.persistence.repository.TransactionRegisterRepository;
import ua.com.alevel.persistence.type.TransactionType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionServiceTest {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final Integer AGE = 30;
    private static final Integer ACCOUNTS_QTY_1 = 1;
    private static final Integer ACCOUNTS_QTY_2 = 2;
    private static final String ACCOUNT_NUMBER = "UA";
    private static final Boolean ACCOUNT_ACTIVE_STATUS = true;
    private static final BigDecimal ACCOUNT_BALANCE_EMPTY = new BigDecimal("00.00");
    private static final BigDecimal ACCOUNT_BALANCE_NOT_EMPTY = new BigDecimal("100.00");

    private static final TransactionType MONEY_TRANSFER = TransactionType.MONEY_TRANSFER;
    private static final TransactionType ACCOUNT_REPLENISHMENT = TransactionType.ACCOUNT_REPLENISHMENT;


    private static User user1 = new User();
    private static User user2 = new User();
    private static Account account1 = new Account();
    private static Account account2 = new Account();
    private static Account account3 = new Account();
    private static TransactionCategory expenseCategory = new TransactionCategory();
    private static TransactionCategory incomeCategory = new TransactionCategory();


    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    private TransactionRegisterRepository transactionRegisterRepository;


    @BeforeAll
    public void setUp() {
        user1.setFirstName(FIRST_NAME + 1);
        user1.setLastName(LAST_NAME + 1);
        user1.setAge(AGE);
        user1.setAccountsQty(ACCOUNTS_QTY_1);

        account1.setAccountNumber(ACCOUNT_NUMBER + 1);
        account1.setIsActive(ACCOUNT_ACTIVE_STATUS);
        account1.setBalance(ACCOUNT_BALANCE_NOT_EMPTY);
        account1.setUser(user1);

        user2.setFirstName(FIRST_NAME + 2);
        user2.setLastName(LAST_NAME + 2);
        user2.setAge(AGE);
        user2.setAccountsQty(ACCOUNTS_QTY_2);

        account2.setAccountNumber(ACCOUNT_NUMBER + 2);
        account2.setIsActive(ACCOUNT_ACTIVE_STATUS);
        account2.setBalance(ACCOUNT_BALANCE_NOT_EMPTY);
        account2.setUser(user2);

        account3.setAccountNumber(ACCOUNT_NUMBER + 3);
        account3.setIsActive(ACCOUNT_ACTIVE_STATUS);
        account3.setBalance(ACCOUNT_BALANCE_EMPTY);
        account3.setUser(user2);

        expenseCategory.setIsIncome(false);
        expenseCategory.setTransactionType(MONEY_TRANSFER);
        incomeCategory.setIsIncome(true);
        incomeCategory.setTransactionType(ACCOUNT_REPLENISHMENT);

        userService.create(user1);
        userService.create(user2);
        accountService.create(account1);
        accountService.create(account2);
        accountService.create(account3);
        transactionCategoryRepository.save(expenseCategory);
        transactionCategoryRepository.save(incomeCategory);
    }

    @Test
    @Order(1)
    public void shouldBeProcessTransactionWhenAccountReceiverDoesNotExistInSystem() {
        //start conditions
        Account accountNonExistent = null;
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account1);
        transaction.setToAccount(accountNonExistent);
        transaction.setSum(new BigDecimal("10.00"));

        // given
        Executable executable = () -> transactionService.create(transaction); //1L

        // when
        Exception exception = assertThrows(AccountNotFoundException.class, executable);

        // then
        assertThat(exception).isInstanceOf(AccountNotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo("Such account not found!");
    }

    @Test
    @Order(2)
    public void shouldBeProcessTransactionWhenAmountIsNegative() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account1);
        transaction.setToAccount(account2);
        transaction.setSum(new BigDecimal("-10.00"));

        Executable executable = () -> transactionService.create(transaction); //2L

        // when
        Exception exception = assertThrows(NegativeSumOfTransactionException.class, executable);

        // then
        assertThat(exception).isInstanceOf(NegativeSumOfTransactionException.class);
        assertThat(exception.getMessage()).isEqualTo("Amount is less or equal 0!");
    }

    @Test
    @Order(3)
    public void shouldBeProcessTransactionWhenAmountIsZero() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account1);
        transaction.setToAccount(account2);
        transaction.setSum(new BigDecimal("0.00"));

        Executable executable = () -> transactionService.create(transaction); //3L

        // when
        Exception exception = assertThrows(NegativeSumOfTransactionException.class, executable);

        // then
        assertThat(exception).isInstanceOf(NegativeSumOfTransactionException.class);
        assertThat(exception.getMessage()).isEqualTo("Amount is less or equal 0!");
    }

    @Test
    @Order(4)
    public void shouldBeProcessTransactionWhenAmountIsBiggerThanBalance() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account1);
        transaction.setToAccount(account2);
        transaction.setSum(new BigDecimal("500.00"));

        Executable executable = () -> transactionService.create(transaction); //4L

        // when
        Exception exception = assertThrows(NegativeBalanceAccountException.class, executable);

        // then
        assertThat(exception).isInstanceOf(NegativeBalanceAccountException.class);
        assertThat(exception.getMessage()).isEqualTo("Transfer amount can't be more than balance!");
    }

    @Test
    @Order(5)
    public void shouldBeProcessTransactionWhenAccountSenderAndReceiverIsTheSame() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account1);
        transaction.setToAccount(account1);
        transaction.setSum(new BigDecimal("20.00"));

        Executable executable = () -> transactionService.create(transaction); //5L

        // when
        Exception exception = assertThrows(TheSameAccountsOfSenderAndReceiverException.class, executable);

        // then
        assertThat(exception).isInstanceOf(TheSameAccountsOfSenderAndReceiverException.class);
        assertThat(exception.getMessage()).isEqualTo("Can't be the same account in one transaction!");
    }

    @Test
    @Order(6)
    public void shouldBeProcessTransactionWhenAccountsSenderAndReceiverAreOfOneUser() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account2);
        transaction.setToAccount(account3);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        transactionService.create(transaction); //6L; balance after: acc1 - 100, acc2 - 80, acc3 - 20

        // when
        Transaction transactionResult = transactionService.findById(6L);

        // then
        assertThat(transactionResult.getId()).isEqualTo(6L);
        assertThat(transactionResult.getFromAccount()).isEqualTo(account2);
        assertThat(transactionResult.getToAccount()).isEqualTo(account3);
        assertThat(transactionResult.getSum()).isEqualTo(sum);
    }

    @Test
    @Order(7)
    public void shouldChangeBalanceOfSenderAccountAfterSuccessTransactionWhenUsersAreDifferent() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account1);
        transaction.setToAccount(account2);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        transactionService.create(transaction); //7L; balance after: acc1 - 80, acc2 - 100, acc3 - 20

        // when
        Account account = accountService.findById(1L);

        // then
        BigDecimal expectedResult = ACCOUNT_BALANCE_NOT_EMPTY.subtract(sum);
        Assertions.assertEquals(expectedResult, account.getBalance());
    }

    @Test
    @Order(8)
    public void shouldChangeBalanceOfReceiverAccountAfterSuccessTransactionWhenUsersAreDifferent() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account1);
        transaction.setToAccount(account2);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        transactionService.create(transaction); //8L; balance after: acc1 - 60, acc2 - 120, acc3 - 20

        // when
        Account account = accountService.findById(2L);

        // then
        BigDecimal expectedResult = new BigDecimal("120.00"); //100 + 20
        Assertions.assertEquals(expectedResult, account.getBalance());
    }

    @Test
    @Order(9)
    public void shouldChangeBalanceOfSentAccountAfterSuccessTransactionWhenUserIsTheSame() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account2);
        transaction.setToAccount(account3);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        transactionService.create(transaction); //9L; balance after: acc1 - 60, acc2 - 100, acc3 - 40

        // when
        Account account = accountService.findById(2L);

        // then
        BigDecimal expectedResult = new BigDecimal("100.00"); //120 - 20
        Assertions.assertEquals(expectedResult, account.getBalance());
    }

    @Test
    @Order(10)
    public void shouldChangeBalanceOfReceivedAccountAfterSuccessTransactionWhenUserIsTheSame() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account2);
        transaction.setToAccount(account3);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        transactionService.create(transaction); //10L; balance after: acc1 - 60, acc2 - 80, acc3 - 60

        // when
        Account account = accountService.findById(3L);

        // then
        BigDecimal expectedResult = new BigDecimal("60.00"); //40 + 20
        Assertions.assertEquals(expectedResult, account.getBalance());
    }

    @Test
    @Order(11)
    public void shouldBeForTransactionTwoRecordsInTransactionRegister() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account2);
        transaction.setToAccount(account3);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        transactionService.create(transaction); //T:11L, TR:1L,2L; balance after: acc1 - 60, acc2 - 80, acc3 - 60

        // when
        Collection<TransactionRegister> recordsList = transactionRegisterRepository.findRecordByTransactionId(11L);

        // then
        Assertions.assertEquals(2, recordsList.size());
    }

    @Test
    @Order(12)
    public void shouldBeForSenderRecordExpenseCategoryInRegister() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account1);
        transaction.setToAccount(account2);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        transactionService.create(transaction); //T:12L, TR:3L,4L; balance after: acc1 - 40, acc2 - 100, acc3 - 60

        // when
        TransactionRegister record = transactionRegisterRepository.findById(3L).get();

        // then
        Assertions.assertEquals(false, record.getTransactionCategory().getIsIncome());
    }

    @Test
    @Order(13)
    public void shouldBeForReceiverRecordIncomeCategoryInRegister() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account1);
        transaction.setToAccount(account2);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        transactionService.create(transaction); //T:13L, TR:5L,6L; balance after: acc1 - 20, acc2 - 120, acc3 - 60

        // when
        TransactionRegister record = transactionRegisterRepository.findById(6L).get();

        // then
        Assertions.assertEquals(true, record.getTransactionCategory().getIsIncome());
    }

    @Test
    @Order(14)
    public void shouldBeDifferentCategoriesInRegisterForOneTransactionWhenUserIsTheSame() {
        // given
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account2);
        transaction.setToAccount(account3);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        transactionService.create(transaction); //T:14L, TR:7L,8L; balance after: acc1 - 20, acc2 - 100, acc3 - 80

        // when
        TransactionRegister record1 = transactionRegisterRepository.findById(7L).get();
        TransactionRegister record2 = transactionRegisterRepository.findById(8L).get();

        // then
        Assertions.assertEquals(false, record1.getTransactionCategory().getIsIncome());
        Assertions.assertEquals(true, record2.getTransactionCategory().getIsIncome());
        Assertions.assertEquals(true, record1.getTransaction().getId().equals(record2.getTransaction().getId()));
    }

    @Test
    @Order(15)
    public void checkSizeOfAccountStatementWithoutDates() {
        Collection<Transaction> accountTransactions = transactionService.findAllByAccountId(null, null, account1.getId());
        Assertions.assertEquals(4, accountTransactions.size()); //4: test 7,8,12,13
    }

    @Test
    @Order(16)
    public void checkSizeOfAccountStatementWithFromDate() {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account2);
        transaction.setToAccount(account1);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        OffsetDateTime newDate = OffsetDateTime.parse("2023-03-20T08:20:45+03:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        transaction.setCreated(newDate);
        transactionService.create(transaction); //T:15L, TR:9L,10L; balance after: acc1 - 40, acc2 - 80, acc3 - 80

        String dateTimeFrom = "2023-04-01 00:00:00.000000000";
        Timestamp fromDateValue = Timestamp.valueOf(dateTimeFrom);
        Collection<Transaction> accountTransactions = transactionService.findAllByAccountId(fromDateValue, null, account1.getId());
        Assertions.assertEquals(4, accountTransactions.size()); //4: test 7,8,12,13  test 15 isn't in need period
    }

    @Test
    @Order(17)
    public void checkSizeOfAccountStatementWithToDate() {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account2);
        transaction.setToAccount(account1);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        transactionService.create(transaction); //T:16L, TR:11L,12L; balance after: acc1 - 60, acc2 - 60, acc3 - 80

        String dateTimeTo = "2023-04-01 00:00:00.000000000";
        Timestamp toDateValue = Timestamp.valueOf(dateTimeTo);
        Collection<Transaction> accountTransactions = transactionService.findAllByAccountId(null, toDateValue, account1.getId());
        Assertions.assertEquals(1, accountTransactions.size()); //1: test 15
    }

    @Test
    @Order(18)
    public void checkSizeOfAccountStatementWithFromAndToDate() {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account2);
        transaction.setToAccount(account1);
        BigDecimal sum = new BigDecimal("20.00");
        transaction.setSum(sum);
        transactionService.create(transaction); //T:16L, TR:11L,12L; balance after: acc1 - 60, acc2 - 60, acc3 - 80

        String dateTimeFrom = "2023-03-01 00:00:00.000000000";
        String dateTimeTo = "2023-03-31 23:59:59.999999999";
        Timestamp fromDateValue = Timestamp.valueOf(dateTimeFrom);
        Timestamp toDateValue = Timestamp.valueOf(dateTimeTo);
        Collection<Transaction> accountTransactions = transactionService.findAllByAccountId(fromDateValue, toDateValue, account1.getId());
        Assertions.assertEquals(1, accountTransactions.size()); //1: test 15
    }
}
