package com.ledger.repository;

import io.vertx.core.Future;

import java.util.Optional;
import java.util.UUID;

public interface IIdempotencyRepository {

    /**
     * Finds a transaction ID associated with an idempotency key.
     *
     * @param idempotencyKey the idempotency key to search for
     * @return Future containing Optional of transaction ID if found, empty otherwise
     */
    Future<Optional<UUID>> findTransactionIdByKey(String idempotencyKey);

    /**
     * Saves an idempotency key and its associated transaction ID.
     *
     * @param idempotencyKey the idempotency key
     * @param transactionId the associated transaction ID
     * @return Future that completes when the idempotency key is saved
     */
    Future<Void> save(String idempotencyKey, UUID transactionId);
}

