# wallet-server (Spring Boot 3, Java 21)

Production-grade API skeleton with environment profiles, config binding, file+console logging, and Dockerization.

## Quick Start

```bash
# Run in dev
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
# or: mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Build jar
mvn -DskipTests package

# Run
SPRING_PROFILES_ACTIVE=dev java -jar target/wallet-server-0.0.1-SNAPSHOT.jar
```

Endpoints:
- `GET /v1/health/ping` → `pong`
- `GET /actuator/health` → Spring Boot health
- `GET /swagger-ui.html` → OpenAPI UI

## Environment variables (examples)

```bash
export SPRING_PROFILES_ACTIVE=prod
export APP_RPC_ETHURL="https://eth-mainnet.gw.example"
export APP_RPC_BTCURL="https://blockstream.example/api"
export APP_SECURITY_ALLOWORIGINS="https://app.example.com"
export APP_LOG_DIR="/var/log/wallet-server"
```

## Docker

```bash
docker build -t wallet-server:latest .

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
# wallet-service
