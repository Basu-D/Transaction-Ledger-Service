package com.ledger.repository;

import com.ledger.entity.Transaction;
import io.vertx.core.Future;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for transaction data access operations.
 * Follows Interface Segregation Principle - focused interface for transaction operations.
 */
public interface ITransactionRepository {

    /**
     * Saves a transaction to the database.
     *
     * @param transaction the transaction to save
     * @return Future containing the saved transaction
     */
    Future<Transaction> save(Transaction transaction);

    /**
     * Finds a transaction by its unique identifier.
     *
     * @param transactionId the transaction ID
     * @return Future containing Optional of transaction if found, empty otherwise
     */
    Future<Optional<Transaction>> findById(UUID transactionId);

    /**
     * Calculates the current balance for an account.
     *
     * @param accountId the account ID
     * @return Future containing the calculated balance
     */
    Future<Double> calculateBalance(String accountId);
}

