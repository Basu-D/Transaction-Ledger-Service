package com.ledger.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRequestTest {

    @Test
    void testTransactionRequest_Constructor_SetsAllFields() {
        String accountId = "acc-123";
        double amount = 100.0;
        TransactionType type = TransactionType.DEBIT;
        String idempotencyKey = "key-123";

        TransactionRequest request = new TransactionRequest(accountId, amount, type, idempotencyKey);

        assertEquals(accountId, request.accountId);
        assertEquals(amount, request.amount, 0.01);
        assertEquals(type, request.type);
        assertEquals(idempotencyKey, request.idempotencyKey);
    }

    @Test
    void testTransactionRequest_WithCreditType() {
        String accountId = "acc-456";
        double amount = 200.0;
        TransactionType type = TransactionType.CREDIT;
        String idempotencyKey = "key-456";

        TransactionRequest request = new TransactionRequest(accountId, amount, type, idempotencyKey);

        assertEquals(TransactionType.CREDIT, request.type);
        assertEquals(200.0, request.amount, 0.01);
    }

    @Test
    void testTransactionRequest_WithDebitType() {
        String accountId = "acc-789";
        double amount = 50.0;
        TransactionType type = TransactionType.DEBIT;
        String idempotencyKey = "key-789";

        TransactionRequest request = new TransactionRequest(accountId, amount, type, idempotencyKey);

        assertEquals(TransactionType.DEBIT, request.type);
        assertEquals(50.0, request.amount, 0.01);
    }
}

