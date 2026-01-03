package com.ledger.handler;

import com.ledger.config.Constants;
import com.ledger.dto.CreateTransactionRequest;
import com.ledger.dto.TransactionResponse;
import com.ledger.exception.LedgerException;
import com.ledger.service.TransactionService;
import com.ledger.validation.TransactionRequestValidator;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionHandler {

    private static final Logger logger = LoggerFactory.getLogger(TransactionHandler.class);

    private final TransactionService transactionService;
    private final TransactionRequestValidator validator;

    public TransactionHandler(TransactionService transactionService, TransactionRequestValidator validator) {
        this.transactionService = transactionService;
        this.validator = validator;
    }

    public void handleCreateTransaction(RoutingContext context) {
        try {
            JsonObject body = context.body().asJsonObject();
            CreateTransactionRequest request = validator.validate(body);

            transactionService.createTransaction(request)
                    .onSuccess(response -> sendSuccessResponse(context, response))
                    .onFailure(throwable -> handleError(context, throwable));

        } catch (LedgerException e) {
            sendErrorResponse(context, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error processing transaction request", e);
            sendErrorResponse(context, Constants.HTTP_INTERNAL_SERVER_ERROR, 
                    Constants.ERROR_INTERNAL_SERVER + ": " + e.getMessage());
        }
    }

    private void sendSuccessResponse(RoutingContext context, TransactionResponse response) {
        JsonObject jsonResponse = new JsonObject()
                .put("transactionId", response.getTransactionId().toString())
                .put("status", response.getStatus())
                .put("balance", response.getBalance());

        context.response()
                .setStatusCode(Constants.HTTP_OK)
                .putHeader("Content-Type", Constants.CONTENT_TYPE_JSON)
                .end(jsonResponse.encode());
    }

    private void handleError(RoutingContext context, Throwable throwable) {
        logger.error("Error creating transaction", throwable);
        
        if (throwable instanceof LedgerException) {
            LedgerException ledgerException = (LedgerException) throwable;
            sendErrorResponse(context, ledgerException.getStatusCode(), ledgerException.getMessage());
        } else {
            sendErrorResponse(context, Constants.HTTP_INTERNAL_SERVER_ERROR, 
                    Constants.ERROR_INTERNAL_SERVER + ": " + throwable.getMessage());
        }
    }

    private void sendErrorResponse(RoutingContext context, int statusCode, String message) {
        JsonObject error = new JsonObject().put("error", message);
        context.response()
                .setStatusCode(statusCode)
                .putHeader("Content-Type", Constants.CONTENT_TYPE_JSON)
                .end(error.encode());
    }
}

