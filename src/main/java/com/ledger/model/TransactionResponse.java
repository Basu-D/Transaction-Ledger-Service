package com.ledger.model;

import java.util.UUID;

public class TransactionResponse {

    public UUID transactionId;
    public double balance;
    public String status;

    public TransactionResponse(UUID transactionId, double balance, String status) {
        this.transactionId = transactionId;
        this.balance = balance;
        this.status = status;
    }
}
