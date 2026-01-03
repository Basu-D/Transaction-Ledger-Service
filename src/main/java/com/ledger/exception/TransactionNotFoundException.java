package com.ledger.exception;

import com.ledger.config.Constants;

import java.util.UUID;

public class TransactionNotFoundException extends LedgerException {

    public TransactionNotFoundException(UUID transactionId) {
        super(Constants.HTTP_BAD_REQUEST, 
              String.format(Constants.ERROR_TRANSACTION_NOT_FOUND, transactionId));
    }
}

