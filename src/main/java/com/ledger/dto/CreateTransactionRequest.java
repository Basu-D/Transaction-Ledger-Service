package com.ledger.dto;

import com.ledger.model.TransactionType;

public class CreateTransactionRequest {

    private final String accountId;
    private final double amount;
    private final TransactionType type;
    private final String idempotencyKey;

    public CreateTransactionRequest(String accountId, double amount, TransactionType type, String idempotencyKey) {
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.idempotencyKey = idempotencyKey;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }
}

