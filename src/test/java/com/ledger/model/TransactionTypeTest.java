package com.ledger.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeTest {

    @Test
    void testTransactionType_DEBIT_Exists() {
        assertNotNull(TransactionType.DEBIT);
        assertEquals("DEBIT", TransactionType.DEBIT.name());
    }

    @Test
    void testTransactionType_CREDIT_Exists() {
        assertNotNull(TransactionType.CREDIT);
        assertEquals("CREDIT", TransactionType.CREDIT.name());
    }

    @Test
    void testTransactionType_ValueOf_ValidValues() {
        assertEquals(TransactionType.DEBIT, TransactionType.valueOf("DEBIT"));
        assertEquals(TransactionType.CREDIT, TransactionType.valueOf("CREDIT"));
    }

    @Test
    void testTransactionType_ValueOf_InvalidValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            TransactionType.valueOf("INVALID");
        });
    }

    @Test
    void testTransactionType_Values_ContainsBothTypes() {
        TransactionType[] values = TransactionType.values();
        assertEquals(2, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(TransactionType.DEBIT));
        assertTrue(java.util.Arrays.asList(values).contains(TransactionType.CREDIT));
    }
}

