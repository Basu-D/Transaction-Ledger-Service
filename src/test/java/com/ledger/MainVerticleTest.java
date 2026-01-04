package com.ledger;

import com.ledger.db.DatabaseClient;
import com.ledger.service.TransactionService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.sqlclient.SqlClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
class MainVerticleTest {

    @Mock
    private SqlClient sqlClient;

    private Vertx vertx;
    private MainVerticle mainVerticle;

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        this.vertx = vertx;
        this.mainVerticle = new MainVerticle();
        
        // Mock DatabaseClient
        try (MockedStatic<DatabaseClient> mockedDatabaseClient = mockStatic(DatabaseClient.class)) {
            mockedDatabaseClient.when(() -> DatabaseClient.create(any())).thenReturn(sqlClient);
            
            vertx.deployVerticle(mainVerticle)
                    .onComplete(testContext.succeeding(id -> testContext.completeNow()));
        }
    }

    @Test
    void testHandleCreateTransaction_MissingAccountId_Returns400(VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        
        JsonObject requestBody = new JsonObject()
                .put("amount", 100.0)
                .put("type", "DEBIT")
                .put("idempotencyKey", "key-123");

        client.post(8080, "localhost", "/transactions")
                .as(BodyCodec.jsonObject())
                .sendJsonObject(requestBody)
                .onComplete(testContext.succeeding(response -> {
                    assertEquals(400, response.statusCode());
                    assertTrue(response.body().containsKey("error"));
                    testContext.completeNow();
                }));
    }

    @Test
    void testHandleCreateTransaction_InvalidAmount_Returns400(VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        
        JsonObject requestBody = new JsonObject()
                .put("accountId", "acc-123")
                .put("amount", -50.0)
                .put("type", "DEBIT")
                .put("idempotencyKey", "key-123");

        client.post(8080, "localhost", "/transactions")
                .as(BodyCodec.jsonObject())
                .sendJsonObject(requestBody)
                .onComplete(testContext.succeeding(response -> {
                    assertEquals(400, response.statusCode());
                    assertTrue(response.body().containsKey("error"));
                    testContext.completeNow();
                }));
    }

    @Test
    void testHandleCreateTransaction_InvalidType_Returns400(VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        
        JsonObject requestBody = new JsonObject()
                .put("accountId", "acc-123")
                .put("amount", 100.0)
                .put("type", "INVALID")
                .put("idempotencyKey", "key-123");

        client.post(8080, "localhost", "/transactions")
                .as(BodyCodec.jsonObject())
                .sendJsonObject(requestBody)
                .onComplete(testContext.succeeding(response -> {
                    assertEquals(400, response.statusCode());
                    assertTrue(response.body().containsKey("error"));
                    testContext.completeNow();
                }));
    }

    @Test
    void testHandleCreateTransaction_MissingIdempotencyKey_Returns400(VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);
        
        JsonObject requestBody = new JsonObject()
                .put("accountId", "acc-123")
                .put("amount", 100.0)
                .put("type", "DEBIT");

        client.post(8080, "localhost", "/transactions")
                .as(BodyCodec.jsonObject())
                .sendJsonObject(requestBody)
                .onComplete(testContext.succeeding(response -> {
                    assertEquals(400, response.statusCode());
                    assertTrue(response.body().containsKey("error"));
                    testContext.completeNow();
                }));
    }

    @Test
    void testHandleCreateTransaction_EmptyBody_Returns400(VertxTestContext testContext) {
        WebClient client = WebClient.create(vertx);

        client.post(8080, "localhost", "/transactions")
                .as(BodyCodec.jsonObject())
                .send()
                .onComplete(testContext.succeeding(response -> {
                    assertEquals(400, response.statusCode());
                    assertTrue(response.body().containsKey("error"));
                    testContext.completeNow();
                }));
    }
}
