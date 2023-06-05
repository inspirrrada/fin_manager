package ua.com.alevel.exceptions;

public class NegativeSumOfTransactionException extends RuntimeException {

    public NegativeSumOfTransactionException(String msg) {
        super(msg);
    }
}
