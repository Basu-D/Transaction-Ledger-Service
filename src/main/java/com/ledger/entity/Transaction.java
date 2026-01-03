package com.ledger.entity;

import com.ledger.model.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Transaction {

    private final UUID id;
    private final String accountId;
    private final BigDecimal amount;
    private final TransactionType type;
    private final Instant createdAt;

    public Transaction(UUID id, String accountId, BigDecimal amount, TransactionType type, Instant createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

