package com.ledger;

import com.ledger.config.Constants;
import com.ledger.db.DatabaseClient;
import com.ledger.handler.TransactionHandler;
import com.ledger.repository.RepositoryFactory;
import com.ledger.service.TransactionService;
import com.ledger.validation.TransactionRequestValidator;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.sqlclient.SqlClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    private SqlClient sqlClient;

    @Override
    public void start() {
        sqlClient = DatabaseClient.create(vertx);
        
        // Initialize repositories using factory (Dependency Inversion Principle)
        // Factory creates appropriate implementations based on database type
        RepositoryFactory.RepositorySet repositories = RepositoryFactory.createRepositories(sqlClient);

        // Initialize services with repository interfaces (not concrete implementations)
        TransactionService transactionService = new TransactionService(
                repositories.getTransactionRepository(),
                repositories.getAccountRepository(),
                repositories.getIdempotencyRepository()
        );

        // Initialize handlers
        TransactionRequestValidator validator = new TransactionRequestValidator();
        TransactionHandler transactionHandler = new TransactionHandler(transactionService, validator);

        // Setup routes
        Router router = setupRoutes(transactionHandler);

        // Start server
        int port = Integer.parseInt(System.getProperty(Constants.HTTP_PORT_PROPERTY, Constants.DEFAULT_HTTP_PORT));
        
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(port)
                .onSuccess(server -> {
                    logger.info("Transaction Ledger Service started on port {}", port);
                })
                .onFailure(throwable -> {
                    logger.error("Failed to start server on port {}", port, throwable);
                    throw new RuntimeException("Failed to start server", throwable);
                });
    }

    private Router setupRoutes(TransactionHandler transactionHandler) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post(Constants.API_TRANSACTIONS_PATH).handler(transactionHandler::handleCreateTransaction);
        return router;
    }
}
