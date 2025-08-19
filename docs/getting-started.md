# Getting Started

This guide will help you set up and run the Wallet Server on your local machine for development and testing purposes.

## Prerequisites

- Java Development Kit (JDK) 21 or higher
- Maven 3.6+ or use the included Maven wrapper
- Git (optional, for cloning the repository)
- Docker (optional, for containerized deployment)

## Installation

### Clone the Repository

```bash
git clone https://github.com/smallyunet/wallet-server.git
cd wallet-server
```

### Build the Project

Using Maven:

```bash
mvn clean install
```

Using Maven wrapper:

```bash
./mvnw clean install
```

## Running the Server

### Development Mode

Run the server with the default development profile:

```bash
mvn spring-boot:run
```

Or with a specific profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production Mode

Build the JAR file:

```bash
mvn -DskipTests package
```

Run the JAR:

```bash
java -jar target/wallet-server-0.0.1-SNAPSHOT.jar
```

With a specific profile:

```bash
SPRING_PROFILES_ACTIVE=prod java -jar target/wallet-server-0.0.1-SNAPSHOT.jar
```

## Configuration

Basic configuration is located in `application.yml`. Environment-specific configurations are in:

- `application-dev.yml`
- `application-staging.yml`
- `application-prod.yml`
- `application-test.yml`

See the [Configuration Guide](./configuration-guide.md) for details on all available settings.

## Verify the Installation

### Health Check

```bash
curl http://localhost:8080/v1/health/ping
```

Expected response: `pong`

### Spring Boot Actuator Health

```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

### Check Swagger UI

Open in your browser:
```
http://localhost:8080/swagger-ui.html
```

## Testing with Sample Scripts

Several test scripts are provided in the project root:

```bash
# Check ETH balance
./test-eth-balance.sh sepolia 0xYourEthereumAddress

# Get transaction history
./test-eth-transactions.sh sepolia 0xYourEthereumAddress

# Get gas fee suggestions
./test-gas-fees.sh sepolia

# Send a signed transaction
./test-eth-transfer.sh sepolia 0xYourSignedTransactionHex

# Check transaction status
./test-tx-status.sh sepolia 0xTransactionHash

# Get account nonce
./test-get-nonce.sh sepolia 0xYourEthereumAddress
```

## Next Steps

- Review the [API Reference](./api-reference.md) to learn about available endpoints
- Check the [Deployment Guide](./deployment-guide.md) for production deployment
- See [Development Guide](./development-guide.md) for contributing to the project
