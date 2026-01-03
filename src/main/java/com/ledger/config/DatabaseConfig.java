package com.ledger.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;
    private final int poolSize;

    private DatabaseConfig(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.database = builder.database;
        this.user = builder.user;
        this.password = builder.password;
        this.poolSize = builder.poolSize;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public static DatabaseConfig fromEnvironment() {
        Properties props = loadProperties();
        
        return new Builder()
                .host(getEnvOrProperty(Constants.DB_HOST_ENV, props, "db.host", "localhost"))
                .port(parseInt(getEnvOrProperty(Constants.DB_PORT_ENV, props, "db.port", "5432")))
                .database(getEnvOrProperty(Constants.DB_DATABASE_ENV, props, "db.database", "ledger"))
                .user(getEnvOrProperty(Constants.DB_USER_ENV, props, "db.user", "postgres"))
                .password(getEnvOrProperty(Constants.DB_PASSWORD_ENV, props, "db.password", "postgres"))
                .poolSize(Constants.DEFAULT_POOL_SIZE)
                .build();
    }

    private static String getEnvOrProperty(String envKey, Properties props, String propKey, String defaultValue) {
        String envValue = System.getenv(envKey);
        if (envValue != null) {
            return envValue;
        }
        return props.getProperty(propKey, defaultValue);
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid port number: " + value, e);
        }
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException ignored) {
        }
        return props;
    }

    public static class Builder {
        private String host;
        private int port;
        private String database;
        private String user;
        private String password;
        private int poolSize = Constants.DEFAULT_POOL_SIZE;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder poolSize(int poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        public DatabaseConfig build() {
            return new DatabaseConfig(this);
        }
    }
}

