package ua.com.alevel.exceptions;

public class NegativeBalanceAccountException extends RuntimeException {

    public NegativeBalanceAccountException(String msg) {
        super(msg);
    }
}
