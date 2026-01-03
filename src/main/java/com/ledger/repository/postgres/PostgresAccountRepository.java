package com.ledger.repository.postgres;

import com.ledger.repository.IAccountRepository;
import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

public class PostgresAccountRepository implements IAccountRepository {

    private static final String INSERT_ACCOUNT_QUERY = """
        INSERT INTO accounts (id)
        VALUES ($1)
        ON CONFLICT (id) DO NOTHING
        """;

    private final SqlClient sqlClient;

    public PostgresAccountRepository(SqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Override
    public Future<Void> ensureAccountExists(String accountId) {
        return sqlClient
                .preparedQuery(INSERT_ACCOUNT_QUERY)
                .execute(Tuple.of(accountId))
                .mapEmpty();
    }
}

