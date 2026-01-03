package com.ledger.service;

import com.ledger.config.Constants;
import com.ledger.dto.CreateTransactionRequest;
import com.ledger.dto.TransactionResponse;
import com.ledger.entity.Transaction;
import com.ledger.exception.TransactionNotFoundException;
import com.ledger.repository.IAccountRepository;
import com.ledger.repository.IIdempotencyRepository;
import com.ledger.repository.ITransactionRepository;
import com.ledger.repository.RepositoryFactory;
import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionService {

    private final ITransactionRepository transactionRepository;
    private final IAccountRepository accountRepository;
    private final IIdempotencyRepository idempotencyRepository;

    /**
     * Constructor using repository interfaces (Dependency Inversion Principle).
     * Service depends on abstractions, not concrete implementations.
     *
     * @param transactionRepository transaction repository interface
     * @param accountRepository account repository interface
     * @param idempotencyRepository idempotency repository interface
     */
    public TransactionService(ITransactionRepository transactionRepository,
                              IAccountRepository accountRepository,
                              IIdempotencyRepository idempotencyRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.idempotencyRepository = idempotencyRepository;
    }

    /**
     * Convenience constructor that creates repositories from SqlClient.
     * Uses RepositoryFactory to create appropriate implementations.
     *
     * @param sqlClient the database client
     */
    public TransactionService(SqlClient sqlClient) {
        RepositoryFactory.RepositorySet repositories = RepositoryFactory.createRepositories(sqlClient);
        this.transactionRepository = repositories.getTransactionRepository();
        this.accountRepository = repositories.getAccountRepository();
        this.idempotencyRepository = repositories.getIdempotencyRepository();
    }

    public Future<TransactionResponse> createTransaction(CreateTransactionRequest request) {
        return idempotencyRepository.findTransactionIdByKey(request.getIdempotencyKey())
                .compose(existingTransactionId -> {
                    if (existingTransactionId.isPresent()) {
                        return getTransactionResponse(existingTransactionId.get());
                    } else {
                        return createNewTransaction(request);
                    }
                });
    }

    private Future<TransactionResponse> createNewTransaction(CreateTransactionRequest request) {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction(
                transactionId,
                request.getAccountId(),
                BigDecimal.valueOf(request.getAmount()),
                request.getType(),
                java.time.Instant.now()
        );

        return accountRepository.ensureAccountExists(request.getAccountId())
                .compose(v -> transactionRepository.save(transaction))
                .compose(savedTransaction -> 
                    idempotencyRepository.save(request.getIdempotencyKey(), savedTransaction.getId())
                )
                .compose(v -> getTransactionResponse(transactionId));
    }

    private Future<TransactionResponse> getTransactionResponse(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .compose(transactionOpt -> {
                    if (transactionOpt.isEmpty()) {
                        return Future.failedFuture(new TransactionNotFoundException(transactionId));
                    }
                    
                    Transaction transaction = transactionOpt.get();
                    return transactionRepository.calculateBalance(transaction.getAccountId())
                            .map(balance -> new TransactionResponse(
                                    transaction.getId(),
                                    balance,
                                    Constants.TRANSACTION_STATUS_SUCCESS
                            ));
                });
    }
}
