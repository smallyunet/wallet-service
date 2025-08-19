# Configuration Guide

This document provides comprehensive information on configuring the Wallet Server application.

## Configuration Files

Wallet Server uses Spring Boot's configuration system with YAML files and environment variables. The configuration files are structured as follows:

- `application.yml`: Base configuration with defaults
- `application-dev.yml`: Development environment configuration
- `application-staging.yml`: Staging environment configuration
- `application-prod.yml`: Production environment configuration
- `application-test.yml`: Test environment configuration

## Configuration Properties

Below is a detailed reference of all configuration properties available in the application.

### Core Application Properties

| Property | Description | Default | Required |
|----------|-------------|---------|----------|
| `app.env` | Environment name | "default" | No |
| `app.log-dir` | Directory for log files | *none* | No |

### Ethereum RPC Configuration

| Property | Description | Default | Required |
|----------|-------------|---------|----------|
| `app.rpc.eth.mainnet` | Ethereum mainnet RPC URL | *none* | Yes, for mainnet |
| `app.rpc.eth.sepolia` | Ethereum Sepolia testnet RPC URL | *none* | Yes, for sepolia |

### Bitcoin API Configuration

| Property | Description | Default | Required |
|----------|-------------|---------|----------|
| `app.rpc.btc.mainnet` | Bitcoin mainnet API URL | *none* | Yes, for mainnet |
| `app.rpc.btc.testnet` | Bitcoin testnet API URL | *none* | Yes, for testnet |

### Blockscout API Configuration

| Property | Description | Default | Required |
|----------|-------------|---------|----------|
| `app.blockscout.eth.mainnet` | Blockscout API URL for Ethereum mainnet | *none* | Yes, for mainnet |
| `app.blockscout.eth.sepolia` | Blockscout API URL for Ethereum Sepolia | *none* | Yes, for sepolia |

### Security Configuration

| Property | Description | Default | Required |
|----------|-------------|---------|----------|
| `app.security.allowOrigins` | CORS allowed origins | "*" | No |

### Server Configuration

| Property | Description | Default | Required |
|----------|-------------|---------|----------|
| `server.port` | HTTP server port | 8080 | No |

### Logging Configuration

| Property | Description | Default | Required |
|----------|-------------|---------|----------|
| `logging.level.root` | Root logging level | INFO | No |
| `logging.level.com.example.wallet` | Application logging level | INFO | No |

## Environment Variables

All configuration properties can be overridden using environment variables. Spring Boot automatically converts properties to environment variable names according to the following rules:

- Properties are prefixed with uppercase app names
- Dots (.) become underscores (_)
- Kebab-case is converted to snake_case
- Everything is uppercase

### Examples

| Property | Environment Variable |
|----------|---------------------|
| `app.rpc.eth.mainnet` | `APP_RPC_ETH_MAINNET` |
| `app.blockscout.eth.sepolia` | `APP_BLOCKSCOUT_ETH_SEPOLIA` |
| `app.security.allowOrigins` | `APP_SECURITY_ALLOWORIGINS` |
| `server.port` | `SERVER_PORT` |

## Default Configuration

Here's the default configuration from `application.yml`:

```yaml
server:
  port: 8080

app:
  env: "default"
  blockscout:
    eth:
      mainnet: "https://eth.blockscout.com/api/v2/addresses"
      sepolia: "https://eth-sepolia.blockscout.com/api/v2/addresses"
  rpc:
    eth:
      mainnet: "https://eth-mainnet.example"
      sepolia: "https://ethereum-sepolia-rpc.publicnode.com"
    btc:
      mainnet: "https://blockstream.info/api"
      testnet: "https://blockstream.info/testnet/api"
  security:
    allowOrigins: "*"
```

## Profile-Specific Configuration

### Development Profile

Example `application-dev.yml`:

```yaml
app:
  env: "dev"
  log-dir: "./logs"

logging:
  level:
    root: INFO
    com.example.wallet: DEBUG
```

### Production Profile

Example `application-prod.yml`:

```yaml
app:
  env: "prod"
  log-dir: "/var/log/wallet-server"
  security:
    allowOrigins: "https://app.example.com,https://admin.example.com"

logging:
  level:
    root: WARN
    com.example.wallet: INFO
```

## Selecting a Profile

### Running Locally

```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Using Java
java -Dspring.profiles.active=dev -jar wallet-server-0.0.1-SNAPSHOT.jar
```

### Using Environment Variables

```bash
# Unix/Linux/macOS
export SPRING_PROFILES_ACTIVE=prod
java -jar wallet-server-0.0.1-SNAPSHOT.jar

# Windows
set SPRING_PROFILES_ACTIVE=prod
java -jar wallet-server-0.0.1-SNAPSHOT.jar
```

### In Docker

```bash
docker run -e SPRING_PROFILES_ACTIVE=prod -p 8080:8080 wallet-server:latest
```

## Configuring External Services

### Ethereum RPC Providers

The application needs access to Ethereum JSON-RPC endpoints for functionality like balance checking and transaction broadcasting. Some options include:

1. **Public nodes** - Services like Infura, Alchemy, or Ankr
2. **Self-hosted nodes** - Running your own Ethereum node
3. **Public gateways** - Public endpoints like those provided by publicnode.com

Example configuration:

```yaml
app:
  rpc:
    eth:
      mainnet: "https://mainnet.infura.io/v3/YOUR_API_KEY"
      sepolia: "https://sepolia.infura.io/v3/YOUR_API_KEY"
```

### Bitcoin API

For Bitcoin functionality, the application integrates with blockchain explorers that provide REST APIs:

1. **Blockstream.info API** - Default, no API key required
2. **BlockCypher** - Alternative with API key requirements

Example configuration:

```yaml
app:
  rpc:
    btc:
      mainnet: "https://blockstream.info/api"
      testnet: "https://blockstream.info/testnet/api"
```

### Blockscout API

For Ethereum transaction history, the application uses Blockscout's API:

Example configuration:

```yaml
app:
  blockscout:
    eth:
      mainnet: "https://eth.blockscout.com/api/v2/addresses"
      sepolia: "https://eth-sepolia.blockscout.com/api/v2/addresses"
```

## Configuration Best Practices

1. **Never commit sensitive information**: Use environment variables for API keys and sensitive configuration
2. **Use specific profiles**: Configure each environment with an appropriate profile
3. **Validate configuration**: The application validates critical configuration on startup
4. **Use secure RPC endpoints**: Use HTTPS for all RPC and API endpoints
5. **Set appropriate CORS policies**: Restrict allowed origins in production
6. **Configure logging appropriately**: Set more verbose logging in development, more restrictive in production

## Troubleshooting Configuration Issues

### Missing or Invalid Configuration

If the application fails to start due to missing or invalid configuration, check:

1. That all required properties are provided
2. Environment variables are correctly formatted
3. The active profile is correctly set
4. YAML syntax is valid in configuration files

### RPC Connection Issues

If the application starts but fails to connect to RPC endpoints:

1. Verify RPC URLs are correct and accessible
2. Check network connectivity from the application to the RPC provider
3. Verify API keys are valid (if required)
4. Check for rate limiting issues with the RPC provider

### Logging Configuration

If logs are not appearing as expected:

1. Check the `app.log-dir` setting
2. Verify file permissions for the log directory
3. Ensure the logging level is appropriate for your needs
