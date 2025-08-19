# Introduction

## Overview

Wallet Server is a robust, production-ready API service for blockchain wallet applications. It provides essential backend functionality for cryptocurrency wallets, including balance inquiries, transaction history retrieval, gas fee estimation, and transaction broadcasting.

The server is built with Spring Boot 3 and Java 21, offering a stable and scalable foundation for wallet applications. It supports multiple blockchain networks, including Ethereum (ETH) and Bitcoin (BTC), with the ability to easily expand to other networks.

## Key Features

- **Multi-blockchain Support**: Currently supports Ethereum and Bitcoin networks
- **Network Flexibility**: Configurable to work with mainnet, testnets, and private networks
- **Transaction Broadcasting**: Send signed transactions to the blockchain
- **Balance Queries**: Get wallet balances across different networks
- **Transaction History**: Retrieve transaction history with Blockscout integration
- **Gas Fee Estimation**: Get suggested gas fees for different confirmation times
- **Transaction Status**: Check the status and details of transactions
- **Account Nonce**: Retrieve the current nonce for Ethereum accounts

## Use Cases

Wallet Server is designed for:

- Mobile wallet applications requiring a backend API
- Web-based cryptocurrency dashboards
- Cryptocurrency exchange platforms
- DApp backends requiring blockchain interaction
- Enterprise blockchain applications

## Architecture Overview

Wallet Server follows a clean, layered architecture:

1. **Web Layer**: REST controllers handle HTTP requests and responses
2. **Service Layer**: Business logic and orchestration
3. **Infrastructure Layer**: Integration with blockchain nodes and external APIs
4. **Domain Layer**: Data models and business entities

## Security Considerations

The server is designed with security in mind:

- Client-side transaction signing (private keys never touch the server)
- Configurable CORS settings
- No database or persistent storage of sensitive information
- JSON validation and input sanitization

## Next Steps

- See [Getting Started](./getting-started.md) for installation and setup instructions
- Check the [API Reference](./api-reference.md) for available endpoints
- Review the [Deployment Guide](./deployment-guide.md) for production deployment options
