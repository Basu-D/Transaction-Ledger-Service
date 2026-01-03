package com.ledger.repository;

import com.ledger.entity.Transaction;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class TransactionRepository {

    private static final String INSERT_TRANSACTION_QUERY = """
        INSERT INTO transactions (id, account_id, amount, type)
        VALUES ($1, $2, $3, $4)
        """;

    private static final String FIND_BY_ID_QUERY = """
        SELECT id, account_id, amount, type, created_at
        FROM transactions
        WHERE id = $1
        """;

    private static final String CALCULATE_BALANCE_QUERY = """
        SELECT 
            COALESCE(SUM(
                CASE 
                    WHEN type = 'CREDIT' THEN amount
                    WHEN type = 'DEBIT' THEN -amount
                    ELSE 0
                END
            ), 0) as balance
        FROM transactions
        WHERE account_id = $1
        """;

    private final SqlClient sqlClient;

    public TransactionRepository(SqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    public Future<Transaction> save(Transaction transaction) {
        return sqlClient
                .preparedQuery(INSERT_TRANSACTION_QUERY)
                .execute(Tuple.of(
                        transaction.getId(),
                        transaction.getAccountId(),
                        transaction.getAmount(),
                        transaction.getType().name()
                ))
                .map(rows -> transaction);
    }

    public Future<Optional<Transaction>> findById(UUID transactionId) {
        return sqlClient
                .preparedQuery(FIND_BY_ID_QUERY)
                .execute(Tuple.of(transactionId))
                .map(rows -> {
                    if (rows.size() == 0) {
                        return Optional.empty();
                    }
                    Row row = rows.iterator().next();
                    return Optional.of(mapRowToTransaction(row));
                });
    }

    public Future<Double> calculateBalance(String accountId) {
        return sqlClient
                .preparedQuery(CALCULATE_BALANCE_QUERY)
                .execute(Tuple.of(accountId))
                .map(rows -> {
                    if (rows.size() > 0) {
                        BigDecimal balance = rows.iterator().next().getBigDecimal("balance");
                        return balance != null ? balance.doubleValue() : 0.0;
                    }
                    return 0.0;
                });
    }

    private Transaction mapRowToTransaction(Row row) {
        return new Transaction(
                row.getUUID("id"),
                row.getString("account_id"),
                row.getBigDecimal("amount"),
                com.ledger.model.TransactionType.valueOf(row.getString("type")),
                row.getOffsetDateTime("created_at").toInstant()
        );
    }
}

