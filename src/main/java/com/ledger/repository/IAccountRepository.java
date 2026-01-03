package com.ledger.repository;

import io.vertx.core.Future;

public interface IAccountRepository {

    /**
     * Ensures an account exists in the database.
     *
     * @param accountId the account ID to ensure exists
     * @return Future that completes when the account is ensured to exist
     */
    Future<Void> ensureAccountExists(String accountId);
}

