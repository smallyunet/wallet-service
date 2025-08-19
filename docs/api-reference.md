# API Reference

This document provides a comprehensive reference for all API endpoints available in the Wallet Server.

## Base URL

All API paths are relative to the base URL:

```
http://<host>:<port>/v1
```

For example, if running locally on the default port, the base URL would be:

```
http://localhost:8080/v1
```

## Authentication

Currently, the API does not require authentication. For production deployments, it's recommended to place the API behind an API gateway or proxy server that handles authentication.

## Response Format

All API responses are in JSON format with standard HTTP status codes.

Successful responses typically follow this structure:

```json
{
  "field1": "value1",
  "field2": "value2"
}
```

Error responses follow this structure:

```json
{
  "error": "Error description",
  "status": 400,
  "timestamp": "2025-08-19T15:30:45Z",
  "path": "/v1/eth/sepolia/balance"
}
```

## Endpoints Overview

### Health Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/health/ping` | Simple health check endpoint |

### Ethereum (ETH) Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/eth/{network}/{address}/balance` | Get ETH balance |
| GET | `/eth/{network}/{address}/transactions` | Get transaction history |
| GET | `/eth/{network}/{address}/transactions/raw` | Get raw transaction history |
| GET | `/eth/{network}/gas-fees` | Get gas fee suggestions |
| GET | `/eth/{network}/tx/{txHash}` | Get transaction status and details |
| GET | `/eth/{network}/{address}/nonce` | Get the current nonce for an address |
| POST | `/eth/{network}/transfer` | Send a signed transaction |
| GET | `/eth/{network}/config/rpc` | Get RPC endpoint information |

### Bitcoin (BTC) Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/btc/{network}/{address}/balance` | Get BTC balance |

## Detailed API Specifications

### Health Endpoints

#### GET `/health/ping`

Simple endpoint to verify that the API is running.

**Response:**
```
pong
```

### Ethereum (ETH) Endpoints

#### GET `/eth/{network}/{address}/balance`

Get the ETH balance for a specific address.

**Path Parameters:**
- `network`: The Ethereum network (e.g., `mainnet`, `sepolia`)
- `address`: The Ethereum address

**Response:**
```json
{
  "coin": "ETH",
  "network": "sepolia",
  "address": "0xabcdef1234567890abcdef1234567890abcdef12",
  "balance": "0x1a2b3c4d5e"
}
```

#### GET `/eth/{network}/{address}/transactions`

Get transaction history for an address using Blockscout API.

**Path Parameters:**
- `network`: The Ethereum network (e.g., `mainnet`, `sepolia`)
- `address`: The Ethereum address

**Query Parameters:**
- `filter` (optional): Transaction filter (e.g., `to`, `from`)

**Response:**
```json
{
  "items": [
    {
      "hash": "0x1234...",
      "block_number": 123456,
      "from": {
        "hash": "0xabcd..."
      },
      "to": {
        "hash": "0xefgh..."
      },
      "value": "1000000000000000000",
      "fee": {
        "type": "gas_fee",
        "value": "21000"
      },
      "timestamp": "2025-08-19T12:34:56Z",
      "status": "ok"
    }
  ],
  "next_page_params": null
}
```

#### GET `/eth/{network}/gas-fees`

Get gas fee suggestions for the specified network.

**Path Parameters:**
- `network`: The Ethereum network (e.g., `mainnet`, `sepolia`)

**Response:**
```json
{
  "base_fee": "25.45",
  "unit": "gwei",
  "slow": {
    "max_fee": "25.45",
    "max_priority_fee": "2.55",
    "estimated_seconds": 120
  },
  "average": {
    "max_fee": "30.54",
    "max_priority_fee": "5.09",
    "estimated_seconds": 60
  },
  "fast": {
    "max_fee": "38.18",
    "max_priority_fee": "7.64",
    "estimated_seconds": 30
  },
  "fastest": {
    "max_fee": "50.90",
    "max_priority_fee": "12.73",
    "estimated_seconds": 15
  }
}
```

#### GET `/eth/{network}/tx/{txHash}`

Get transaction status and details by transaction hash.

**Path Parameters:**
- `network`: The Ethereum network (e.g., `mainnet`, `sepolia`)
- `txHash`: The transaction hash

**Response:**
```json
{
  "transaction_hash": "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
  "block_hash": "0xabcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890",
  "block_number": "12345678",
  "from": "0x1234567890abcdef1234567890abcdef12345678",
  "to": "0xabcdef1234567890abcdef1234567890abcdef12",
  "gas_used": "21000",
  "gas_price": "50000000000",
  "effective_gas_price": "50000000000",
  "value": "1000000000000000000",
  "status": "success",
  "cumulative_gas_used": "12500000",
  "logs_bloom": "0x0000...",
  "confirmation_count": 10,
  "logs": [
    {
      "address": "0x1234...",
      "topics": ["0xabcd..."],
      "data": "0x0000...",
      "log_index": "0",
      "block_number": "12345678",
      "block_hash": "0xabcd...",
      "transaction_index": "0"
    }
  ]
}
```

#### GET `/eth/{network}/{address}/nonce`

Get the current nonce for an Ethereum address.

**Path Parameters:**
- `network`: The Ethereum network (e.g., `mainnet`, `sepolia`)
- `address`: The Ethereum address

**Response:**
```json
{
  "address": "0x1234567890abcdef1234567890abcdef12345678",
  "network": "sepolia",
  "nonce": "0xa",
  "nonce_decimal": 10
}
```

#### POST `/eth/{network}/transfer`

Send a signed transaction to the Ethereum network.

**Path Parameters:**
- `network`: The Ethereum network (e.g., `mainnet`, `sepolia`)

**Request Body:**
```json
{
  "signed_transaction": "0x1234567890abcdef...",
  "from": "0x1234567890abcdef1234567890abcdef12345678", // optional
  "to": "0xabcdef1234567890abcdef1234567890abcdef12",   // optional
  "value": "0x1bc16d674ec80000"                         // optional
}
```

**Response:**
```json
{
  "transaction_hash": "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
  "from": "0x1234567890abcdef1234567890abcdef12345678",
  "to": "0xabcdef1234567890abcdef1234567890abcdef12",
  "value": "0x1bc16d674ec80000",
  "block_hash": "0xabcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890",
  "block_number": "12345678",
  "gas_used": "21000",
  "status": "pending"
}
```

#### GET `/eth/{network}/config/rpc`

Get the configured RPC URL for the specified network.

**Path Parameters:**
- `network`: The Ethereum network (e.g., `mainnet`, `sepolia`)

**Response:**
```
https://eth-sepolia.provider.com
```

### Bitcoin (BTC) Endpoints

#### GET `/btc/{network}/{address}/balance`

Get the BTC balance for a specific address.

**Path Parameters:**
- `network`: The Bitcoin network (e.g., `mainnet`, `testnet`)
- `address`: The Bitcoin address

**Response:**
```json
{
  "coin": "BTC",
  "network": "mainnet",
  "address": "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa",
  "balance": "123456789"
}
```

## Error Codes

| Status Code | Description |
|-------------|-------------|
| 400 | Bad Request - The request was malformed or contains invalid parameters |
| 404 | Not Found - The requested resource was not found |
| 422 | Unprocessable Entity - The request was well-formed but contains semantic errors |
| 500 | Internal Server Error - An unexpected error occurred on the server |

## Rate Limiting

Currently, there are no rate limits implemented at the API level. However, upstream services (like RPC providers) may have their own rate limits. For production use, consider implementing rate limiting at the API gateway level.

## Versioning

The API uses URL versioning with the format `/v{number}`. The current version is `/v1`. When breaking changes are introduced, a new version will be created (e.g., `/v2`).
