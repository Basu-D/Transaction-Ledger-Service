package com.ledger.repository;

import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

import java.util.Optional;
import java.util.UUID;

public class IdempotencyRepository {

    private static final String FIND_BY_KEY_QUERY = """
        SELECT transaction_id
        FROM idempotency_keys
        WHERE idempotency_key = $1
        """;

    private static final String SAVE_QUERY = """
        INSERT INTO idempotency_keys (idempotency_key, transaction_id)
        VALUES ($1, $2)
        """;

    private final SqlClient sqlClient;

    public IdempotencyRepository(SqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    public Future<Optional<UUID>> findTransactionIdByKey(String idempotencyKey) {
        return sqlClient
                .preparedQuery(FIND_BY_KEY_QUERY)
                .execute(Tuple.of(idempotencyKey))
                .map(rows -> {
                    if (rows.size() > 0) {
                        UUID transactionId = rows.iterator().next().getUUID("transaction_id");
                        return Optional.of(transactionId);
                    }
                    return Optional.empty();
                });
    }

    public Future<Void> save(String idempotencyKey, UUID transactionId) {
        return sqlClient
                .preparedQuery(SAVE_QUERY)
                .execute(Tuple.of(idempotencyKey, transactionId))
                .mapEmpty();
    }
}

