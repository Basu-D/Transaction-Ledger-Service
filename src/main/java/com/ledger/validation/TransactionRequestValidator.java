package com.ledger.validation;

import com.ledger.config.Constants;
import com.ledger.dto.CreateTransactionRequest;
import com.ledger.exception.LedgerException;
import com.ledger.model.TransactionType;
import io.vertx.core.json.JsonObject;

public class TransactionRequestValidator {

    public CreateTransactionRequest validate(JsonObject body) {
        if (body == null) {
            throw new LedgerException(Constants.HTTP_BAD_REQUEST, Constants.ERROR_REQUEST_BODY_REQUIRED);
        }

        String accountId = body.getString("accountId");
        if (accountId == null || accountId.isBlank()) {
            throw new LedgerException(Constants.HTTP_BAD_REQUEST, Constants.ERROR_ACCOUNT_ID_REQUIRED);
        }

        Double amount = body.getDouble("amount");
        if (amount == null || amount <= 0) {
            throw new LedgerException(Constants.HTTP_BAD_REQUEST, Constants.ERROR_AMOUNT_INVALID);
        }

        String typeStr = body.getString("type");
        if (typeStr == null || typeStr.isBlank()) {
            throw new LedgerException(Constants.HTTP_BAD_REQUEST, Constants.ERROR_TYPE_REQUIRED);
        }

        TransactionType type;
        try {
            type = TransactionType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new LedgerException(Constants.HTTP_BAD_REQUEST, Constants.ERROR_TYPE_INVALID);
        }

        String idempotencyKey = body.getString("idempotencyKey");
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new LedgerException(Constants.HTTP_BAD_REQUEST, Constants.ERROR_IDEMPOTENCY_KEY_REQUIRED);
        }

        return new CreateTransactionRequest(accountId, amount, type, idempotencyKey);
    }
}

