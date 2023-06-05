package ua.com.alevel.exceptions;

public class TheSameAccountsOfSenderAndReceiverException extends RuntimeException {

    public TheSameAccountsOfSenderAndReceiverException(String msg) {
        super(msg);
    }
}
