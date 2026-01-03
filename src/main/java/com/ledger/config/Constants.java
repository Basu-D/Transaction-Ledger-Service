package com.ledger.config;

public final class Constants {

    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }

    // HTTP Status Codes
    public static final int HTTP_OK = 200;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;

    // Transaction Status
    public static final String TRANSACTION_STATUS_SUCCESS = "SUCCESS";

    // HTTP Headers
    public static final String CONTENT_TYPE_JSON = "application/json";

    // Configuration Keys
    public static final String HTTP_PORT_PROPERTY = "http.port";
    public static final String DEFAULT_HTTP_PORT = "8080";

    // Database Configuration Keys
    public static final String DB_HOST_ENV = "DB_HOST";
    public static final String DB_PORT_ENV = "DB_PORT";
    public static final String DB_DATABASE_ENV = "DB_DATABASE";
    public static final String DB_USER_ENV = "DB_USER";
    public static final String DB_PASSWORD_ENV = "DB_PASSWORD";

    // API Paths
    public static final String API_TRANSACTIONS_PATH = "/transactions";

    // Error Messages
    public static final String ERROR_REQUEST_BODY_REQUIRED = "Request body is required";
    public static final String ERROR_ACCOUNT_ID_REQUIRED = "accountId is required";
    public static final String ERROR_AMOUNT_INVALID = "amount must be a positive number";
    public static final String ERROR_TYPE_REQUIRED = "type is required (DEBIT or CREDIT)";
    public static final String ERROR_TYPE_INVALID = "type must be either DEBIT or CREDIT";
    public static final String ERROR_IDEMPOTENCY_KEY_REQUIRED = "idempotencyKey is required";
    public static final String ERROR_INTERNAL_SERVER = "Internal server error";
    public static final String ERROR_TRANSACTION_NOT_FOUND = "Transaction not found: %s";

    // Database Pool Configuration
    public static final int DEFAULT_POOL_SIZE = 10;
}

