package com.ledger.repository;

import com.ledger.repository.postgres.PostgresAccountRepository;
import com.ledger.repository.postgres.PostgresIdempotencyRepository;
import com.ledger.repository.postgres.PostgresTransactionRepository;
import io.vertx.sqlclient.SqlClient;


public class RepositoryFactory {

    /**
     * Creates repository implementations based on the database type.
     * Currently supports PostgreSQL. Can be extended to support other databases.
     *
     * @param sqlClient the database client
     * @return RepositorySet containing all repository implementations
     */
    public static RepositorySet createRepositories(SqlClient sqlClient) {
        return createPostgresRepositories(sqlClient);
    }

    /**
     * Creates PostgreSQL-specific repository implementations.
     *
     * @param sqlClient the PostgreSQL client
     * @return RepositorySet with PostgreSQL implementations
     */
    private static RepositorySet createPostgresRepositories(SqlClient sqlClient) {
        ITransactionRepository transactionRepository = new PostgresTransactionRepository(sqlClient);
        IAccountRepository accountRepository = new PostgresAccountRepository(sqlClient);
        IIdempotencyRepository idempotencyRepository = new PostgresIdempotencyRepository(sqlClient);

        return new RepositorySet(transactionRepository, accountRepository, idempotencyRepository);
    }

    /**
     * Container class for all repository implementations.
     * Follows Single Responsibility Principle - holds repository instances.
     */
    public static class RepositorySet {
        private final ITransactionRepository transactionRepository;
        private final IAccountRepository accountRepository;
        private final IIdempotencyRepository idempotencyRepository;

        public RepositorySet(ITransactionRepository transactionRepository,
                            IAccountRepository accountRepository,
                            IIdempotencyRepository idempotencyRepository) {
            this.transactionRepository = transactionRepository;
            this.accountRepository = accountRepository;
            this.idempotencyRepository = idempotencyRepository;
        }

        public ITransactionRepository getTransactionRepository() {
            return transactionRepository;
        }

        public IAccountRepository getAccountRepository() {
            return accountRepository;
        }

        public IIdempotencyRepository getIdempotencyRepository() {
            return idempotencyRepository;
        }
    }
}

