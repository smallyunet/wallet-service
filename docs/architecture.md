# Architecture

This document describes the architectural design of the Wallet Server application.

## High-Level Architecture

Wallet Server follows a layered architecture pattern with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────────┐
│                        HTTP Request Layer                       │
│               (Controllers, Request Validation)                 │
└───────────────────────────────┬─────────────────────────────────┘
                               │
┌───────────────────────────────▼─────────────────────────────────┐
│                         Service Layer                           │
│               (Business Logic, Orchestration)                   │
└───────────────────────────────┬─────────────────────────────────┘
                               │
┌───────────────────────────────▼─────────────────────────────────┐
│                     Infrastructure Layer                        │
│           (External API Integration, Blockchain RPC)            │
└─────────────────────────────────────────────────────────────────┘
```

## Component Diagram

```
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│ Controllers   │───►│   Services    │───►│ Infrastructure│
│ - Health      │    │ - Balance     │    │ - EthClient   │
│ - Eth         │    │ - Blockscout  │    │ - BtcClient   │
│ - Btc         │    │               │    │ - Blockscout  │
└───────────────┘    └───────────────┘    └───────┬───────┘
                                                 │
                                          ┌──────▼──────┐
                                          │  External   │
                                          │  Services   │
                                          │ - ETH Nodes │
                                          │ - BTC APIs  │
                                          │ - Blockscout│
                                          └─────────────┘
```

## Layers

### 1. HTTP Request Layer (Web)

**Purpose**: Handle HTTP requests, validate inputs, and format responses.

**Key Components**:
- `EthController`: Handles Ethereum-related endpoints
- `BtcController`: Handles Bitcoin-related endpoints
- `HealthController`: Provides health check endpoints

**Responsibilities**:
- Request routing and parameter validation
- Mapping between DTOs and service layer models
- Error handling and HTTP status code management
- Response formatting

### 2. Service Layer

**Purpose**: Implement business logic and coordinate between multiple infrastructure components.

**Key Components**:
- `BalanceService`: Handles balance and transaction-related operations
- `BlockscoutService`: Manages interaction with Blockscout API

**Responsibilities**:
- Orchestrating operations that span multiple infrastructure components
- Implementing business rules and validations
- Transforming data between different formats
- Error handling and logging

### 3. Infrastructure Layer

**Purpose**: Provide integration with external systems and services.

**Key Components**:
- `EthClient`: Interacts with Ethereum nodes through Web3j
- `BtcClient`: Interacts with Bitcoin API endpoints
- `BlockscoutProvider`: Interfaces with the Blockscout API

**Responsibilities**:
- Establishing connections with external services
- Handling protocol-specific details
- Managing network errors and retries
- Parsing external service responses

## Data Flow

1. HTTP request arrives at a controller
2. Controller validates request parameters and passes them to the service layer
3. Service layer performs business logic and calls infrastructure components
4. Infrastructure components interact with external services
5. Results propagate back up through the layers
6. Controller formats the response and sends it back to the client

## Dependency Injection

The application uses Spring's dependency injection to manage component lifecycle and dependencies. Each layer only depends on the layer directly below it, ensuring clean separation of concerns.

## Cross-Cutting Concerns

### Configuration

- `AppProperties`: Central configuration class bound to application.yml and environment variables
- Environment-specific profiles (dev, test, staging, prod)

### Exception Handling

- `GlobalExceptionHandler`: Centralized exception handling with appropriate HTTP status codes
- Custom exception types for different error scenarios

### Logging

- SLF4J with Logback implementation
- Configurable log levels and destinations
- Request/response logging for debugging

### Security

- CORS configuration in `WebConfig`
- No authentication mechanism in the API itself (assumed to be behind an API gateway)

## Integration Points

### Ethereum Integration

- Web3j library for JSON-RPC interaction with Ethereum nodes
- Support for mainnet and testnet networks
- Functions for balance queries, transaction broadcasting, and gas estimation

### Bitcoin Integration

- HTTP client for Bitcoin blockchain explorer APIs
- Support for mainnet and testnet networks
- Functions for balance queries

### Blockscout Integration

- HTTP client for Blockscout API
- Transaction history retrieval and parsing

## Scalability Considerations

1. **Stateless Design**: The application is completely stateless, allowing for horizontal scaling
2. **Caching**: No caching is currently implemented but could be added for frequently requested data
3. **Connection Pooling**: Web3j and HTTP clients use connection pooling for efficiency
4. **Asynchronous Processing**: Some long-running operations could be made asynchronous in future versions

## Testing Strategy

1. **Unit Tests**: For individual components with mocked dependencies
2. **Integration Tests**: For testing interaction with external services
3. **API Tests**: For testing HTTP endpoints
4. **Load Tests**: For ensuring the application can handle expected traffic

## Future Architectural Considerations

1. **Circuit Breaker Pattern**: To gracefully handle external service failures
2. **Retry Mechanisms**: For transient errors from blockchain nodes
3. **Rate Limiting**: To protect the API from abuse
4. **Caching Layer**: To reduce load on external services
5. **Event-Driven Architecture**: For handling asynchronous operations like transaction status monitoring
