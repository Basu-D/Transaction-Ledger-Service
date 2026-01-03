package com.ledger.dto;

import java.util.UUID;

public class TransactionResponse {

    private final UUID transactionId;
    private final double balance;
    private final String status;

    public TransactionResponse(UUID transactionId, double balance, String status) {
        this.transactionId = transactionId;
        this.balance = balance;
        this.status = status;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public double getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }
}

