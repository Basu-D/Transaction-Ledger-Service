package com.ledger.model;

public class TransactionRequest {

    public String accountId;
    public double amount;
    public TransactionType type;
    public String idempotencyKey;

    public TransactionRequest(String accountId, double amount, TransactionType type, String idempotencyKey) {
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.idempotencyKey = idempotencyKey;
    }
}
