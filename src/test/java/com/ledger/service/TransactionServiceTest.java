package com.ledger.service;

import com.ledger.dto.CreateTransactionRequest;
import com.ledger.model.TransactionType;
import io.vertx.core.Future;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.sqlclient.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
class TransactionServiceTest {

    @Mock
    private SqlClient sqlClient;

    @Mock
    private PreparedQuery<RowSet<Row>> preparedQuery;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(sqlClient);
    }

    @Test
    void testCreateTransaction_DatabaseError_ReturnsFailure(VertxTestContext testContext) {
        // Arrange
        String accountId = "acc-123";
        double amount = 100.0;
        TransactionType type = TransactionType.DEBIT;
        String idempotencyKey = "key-123";

        CreateTransactionRequest request = new CreateTransactionRequest(accountId, amount, type, idempotencyKey);

        // Mock database error
        when(sqlClient.preparedQuery(anyString())).thenReturn(preparedQuery);
        when(preparedQuery.execute(any(Tuple.class)))
                .thenReturn(Future.failedFuture("Database connection failed"));

        // Act & Assert
        transactionService.createTransaction(request)
                .onComplete(testContext.failing(throwable -> {
                    assertNotNull(throwable);
                    assertEquals("Database connection failed", throwable.getMessage());
                    testContext.completeNow();
                }));
    }

    @Test
    void testTransactionService_Constructor_InitializesCorrectly() {
        // Act
        TransactionService service = new TransactionService(sqlClient);

        // Assert
        assertNotNull(service);
    }
}
