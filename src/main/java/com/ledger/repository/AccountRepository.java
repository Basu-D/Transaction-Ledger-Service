package com.ledger.repository;

import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

public class AccountRepository {

    private static final String INSERT_ACCOUNT_QUERY = """
        INSERT INTO accounts (id)
        VALUES ($1)
        ON CONFLICT (id) DO NOTHING
        """;

    private final SqlClient sqlClient;

    public AccountRepository(SqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    public Future<Void> ensureAccountExists(String accountId) {
        return sqlClient
                .preparedQuery(INSERT_ACCOUNT_QUERY)
                .execute(Tuple.of(accountId))
                .mapEmpty();
    }
}

