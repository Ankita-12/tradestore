package com.tradestore.exception;

public class InvalidTradeException extends Exception {

    public InvalidTradeException(final String id) {
        super("Invalid Trade: " + id);
    }
}
