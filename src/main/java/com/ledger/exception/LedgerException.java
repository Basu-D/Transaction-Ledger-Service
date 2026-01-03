package com.ledger.exception;

public class LedgerException extends RuntimeException {

    private final int statusCode;

    public LedgerException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public LedgerException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }

    public LedgerException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public LedgerException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

