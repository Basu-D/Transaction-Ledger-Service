package com.ledger.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionResponseTest {

    @Test
    void testTransactionResponse_Constructor_SetsAllFields() {
        UUID transactionId = UUID.randomUUID();
        double balance = 100.0;
        String status = "SUCCESS";

        TransactionResponse response = new TransactionResponse(transactionId, balance, status);

        assertEquals(transactionId, response.transactionId);
        assertEquals(balance, response.balance, 0.01);
        assertEquals(status, response.status);
    }

    @Test
    void testTransactionResponse_WithNegativeBalance() {
        UUID transactionId = UUID.randomUUID();
        double balance = -50.0;
        String status = "SUCCESS";

        TransactionResponse response = new TransactionResponse(transactionId, balance, status);

        assertEquals(-50.0, response.balance, 0.01);
        assertEquals("SUCCESS", response.status);
    }

    @Test
    void testTransactionResponse_WithZeroBalance() {
        UUID transactionId = UUID.randomUUID();
        double balance = 0.0;
        String status = "SUCCESS";

        TransactionResponse response = new TransactionResponse(transactionId, balance, status);

        assertEquals(0.0, response.balance, 0.01);
    }
}

