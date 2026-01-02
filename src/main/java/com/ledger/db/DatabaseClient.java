package com.ledger.db;

import com.ledger.config.DatabaseConfig;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

public class DatabaseClient {

    public static SqlClient create(Vertx vertx) {
        DatabaseConfig config = DatabaseConfig.fromEnvironment();
        
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setHost(config.getHost())
                .setPort(config.getPort())
                .setDatabase(config.getDatabase())
                .setUser(config.getUser())
                .setPassword(config.getPassword());

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(config.getPoolSize());

        return PgBuilder.pool(builder ->
                builder
                    .connectingTo(connectOptions)
                    .with(poolOptions)
                    .using(vertx)
        );
    }
}
