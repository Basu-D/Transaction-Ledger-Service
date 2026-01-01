# Transaction Ledger Service

## Overview

The Transaction Ledger Service is a backend service that records financial transactions using an **append-only ledger** approach.

The goal of this service is to handle transactions in a way that is **safe, auditable, and predictable**, even when things go wrong — such as network retries, partial failures, or concurrent requests. These are common challenges in transaction-oriented systems, especially in banking and payments.

The service exposes REST APIs to create transactions and fetch balances, while ensuring that duplicate or retried requests do not lead to inconsistent or incorrect state.

---

## Goals

- Store all transactions as immutable ledger entries
- Safely handle duplicate and retried requests using idempotency
- Derive account balances reliably from ledger data
- Remain correct under failures, retries, and concurrent access

---

## Planned for Later Versions

To keep the initial version simple and focused, the following features will be added in later iterations:

- Authentication and authorization
- External payment gateway integrations
- Scaling or sharding strategies

---

## High-Level Design

At a high level, the system processes a transaction request in the following way:

1. A client sends a transaction request via a REST API
2. The service checks whether the request is a retry using an idempotency key
3. The transaction is recorded as an append-only ledger entry
4. The service returns the transaction result along with the updated balance

PostgreSQL is used to provide durable storage and transactional guarantees.  
Kafka is planned to be introduced in later stages to publish transaction events asynchronously.

---

## Tech Stack

- **Language:** Java
- **Framework:** Vert.x
- **Database:** PostgreSQL
- **Messaging (planned):** Kafka
- **API Style:** REST

---

## Core Domain Concepts

- **Account** – Represents an entity that owns a balance
- **Transaction** – An immutable record of a debit or credit operation
- **Ledger** – An append-only collection of transactions
- **Idempotency Key** – A client-provided key used to safely handle retries

---

## API (Initial)

### Create Transaction

POST /transactions


**Request**
```json
{
  "accountId": "acc-123",
  "amount": 100,
  "type": "DEBIT",
  "idempotencyKey": "abc-123"
}
```

**Response**
```json
{
  "transactionId": "txn-456",
  "status": "SUCCESS",
  "balance": 900
}
```