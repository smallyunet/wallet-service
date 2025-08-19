# Wallet Server

![Version](https://img.shields.io/badge/version-0.0.1--SNAPSHOT-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-brightgreen)
![License](https://img.shields.io/badge/license-MIT-green)

A production-ready cryptocurrency wallet server built with Spring Boot 3 and Java 21. This service provides APIs for cryptocurrency balance checking and transaction management with support for multiple blockchain networks including Ethereum and Bitcoin.

## Features

- **Multi-Blockchain Support**: Connects to Ethereum and Bitcoin networks
- **RESTful API Design**: Clean, well-documented API endpoints
- **Environment Profiles**: Development, test, staging, and production environments
- **Configuration Binding**: Type-safe configuration with environment variables
- **Comprehensive Logging**: Both file and console logging with rotation
- **Containerization**: Docker support for easy deployment
- **API Documentation**: Swagger UI for interactive API testing
- **Health Monitoring**: Built-in health check endpoints

## Quick Start

### Prerequisites

- Java 21 or higher
- Maven 3.8.0 or higher
- Docker (optional)

### Development Setup

```bash
# Clone the repository
git clone https://github.com/smallyunet/wallet-server.git
cd wallet-server

# Run in development mode
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
# or: mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Build the application
mvn -DskipTests package

# Run the built JAR
SPRING_PROFILES_ACTIVE=dev java -jar target/wallet-server-0.0.1-SNAPSHOT.jar
```

### Available Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/v1/health/ping` | GET | Simple health check (returns "pong") |
| `/v1/eth/balance/{address}` | GET | Get Ethereum balance for an address |
| `/v1/btc/balance/{address}` | GET | Get Bitcoin balance for an address |
| `/actuator/health` | GET | Spring Boot health indicators |
| `/swagger-ui.html` | GET | OpenAPI UI for testing APIs |

## Configuration

### Environment Variables

```bash
# Required environment variables
export SPRING_PROFILES_ACTIVE=prod              # Active profile (dev, test, staging, prod)
export APP_RPC_ETHURL="https://eth-mainnet.gw.example"    # Ethereum RPC URL
export APP_RPC_BTCURL="https://blockstream.example/api"   # Bitcoin API URL

# Optional environment variables
export APP_SECURITY_ALLOWORIGINS="https://app.example.com"  # CORS allowed origins
export APP_LOG_DIR="/var/log/wallet-server"                 # Log directory
```

### Profile-Specific Configuration

The application supports multiple environment profiles:
- `dev`: Development environment with detailed logging
- `test`: Testing environment
- `staging`: Pre-production environment
- `prod`: Production environment with optimized settings

## Docker Deployment

### Build Docker Image

```bash
docker build -t wallet-server:latest .
```

### Run Docker Container

```bash
docker run -d --name wallet-server \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e APP_RPC_ETHURL="https://eth-mainnet.gw.example" \
  -e APP_RPC_BTCURL="https://blockstream.example/api" \
  -e APP_SECURITY_ALLOWORIGINS="https://app.example.com" \
  -e APP_LOG_DIR="/var/log/wallet-server" \
  -v $(pwd)/logs:/var/log/wallet-server \
  wallet-server:latest
```

## API Documentation

For detailed API documentation, please access the Swagger UI at `/swagger-ui.html` after starting the server.

## Project Structure

```
src/
  ├── main/
  │   ├── java/com/example/wallet/
  │   │   ├── config/           # Application configuration
  │   │   ├── domain/           # Domain models
  │   │   ├── exception/        # Exception handling
  │   │   ├── infra/           
  │   │   │   ├── btc/          # Bitcoin client integration
  │   │   │   └── eth/          # Ethereum client integration
  │   │   ├── service/          # Business logic services
  │   │   └── web/              # REST controllers
  │   └── resources/
  │       ├── application.yml   # Common application properties
  │       └── application-*.yml # Environment-specific properties
  └── test/                     # Test classes
```

## Monitoring and Logging

- Logs are written to both console and files
- Log files are stored in the configured directory (default: `./logs/`)
- Log rotation is configured to prevent excessive disk usage

## Contributing

Please read our [Contributing Guidelines](./docs/development-guide.md) for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
