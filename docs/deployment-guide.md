# Deployment Guide

This guide covers the deployment options and best practices for running Wallet Server in various environments.

## Deployment Options

### 1. Standalone JAR Deployment

The simplest deployment method is running the application as a standalone JAR file.

#### Build the JAR

```bash
mvn -DskipTests package
```

#### Run the JAR

```bash
java -jar target/wallet-server-0.0.1-SNAPSHOT.jar
```

With specific profile and memory settings:

```bash
SPRING_PROFILES_ACTIVE=prod java -Xms512m -Xmx1g -jar target/wallet-server-0.0.1-SNAPSHOT.jar
```

### 2. Docker Deployment

#### Build the Docker Image

```bash
docker build -t wallet-server:latest .
```

#### Run the Docker Container

```bash
docker run -d --name wallet-server \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e APP_RPC_ETH_MAINNET="https://eth-mainnet.provider.com" \
  -e APP_RPC_ETH_SEPOLIA="https://eth-sepolia.provider.com" \
  -e APP_BLOCKSCOUT_ETH_MAINNET="https://eth.blockscout.com/api/v2/addresses" \
  -e APP_BLOCKSCOUT_ETH_SEPOLIA="https://eth-sepolia.blockscout.com/api/v2/addresses" \
  -e APP_SECURITY_ALLOWORIGINS="https://yourapp.com" \
  -v $(pwd)/logs:/var/log/wallet-server \
  wallet-server:latest
```

### 3. Kubernetes Deployment

#### Example Kubernetes Deployment YAML

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: wallet-server
  labels:
    app: wallet-server
spec:
  replicas: 3
  selector:
    matchLabels:
      app: wallet-server
  template:
    metadata:
      labels:
        app: wallet-server
    spec:
      containers:
      - name: wallet-server
        image: wallet-server:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: APP_RPC_ETH_MAINNET
          valueFrom:
            secretKeyRef:
              name: wallet-server-secrets
              key: eth-mainnet-rpc
        - name: APP_RPC_ETH_SEPOLIA
          valueFrom:
            secretKeyRef:
              name: wallet-server-secrets
              key: eth-sepolia-rpc
        resources:
          limits:
            cpu: "1"
            memory: "1Gi"
          requests:
            cpu: "0.5"
            memory: "512Mi"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /v1/health/ping
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
```

#### Example Kubernetes Service YAML

```yaml
apiVersion: v1
kind: Service
metadata:
  name: wallet-server
spec:
  selector:
    app: wallet-server
  ports:
  - port: 80
    targetPort: 8080
  type: ClusterIP
```

## Environment Configuration

### Essential Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `prod`, `staging` |
| `APP_RPC_ETH_MAINNET` | Ethereum Mainnet RPC URL | `https://eth-mainnet.provider.com` |
| `APP_RPC_ETH_SEPOLIA` | Ethereum Sepolia RPC URL | `https://eth-sepolia.provider.com` |
| `APP_RPC_BTC_MAINNET` | Bitcoin Mainnet API URL | `https://blockstream.info/api` |
| `APP_RPC_BTC_TESTNET` | Bitcoin Testnet API URL | `https://blockstream.info/testnet/api` |
| `APP_BLOCKSCOUT_ETH_MAINNET` | Blockscout API URL for ETH mainnet | `https://eth.blockscout.com/api/v2/addresses` |
| `APP_BLOCKSCOUT_ETH_SEPOLIA` | Blockscout API URL for ETH sepolia | `https://eth-sepolia.blockscout.com/api/v2/addresses` |
| `APP_SECURITY_ALLOWORIGINS` | CORS allowed origins | `https://app.example.com` |
| `APP_LOG_DIR` | Log directory | `/var/log/wallet-server` |

## Scaling Considerations

### Horizontal Scaling

Wallet Server is stateless and can be horizontally scaled by deploying multiple instances behind a load balancer.

### Resource Requirements

Minimum recommended resources per instance:
- CPU: 1 core
- Memory: 512MB
- Disk: 1GB for application and logs

## Monitoring and Maintenance

### Health Endpoints

- `/actuator/health`: Spring Boot health information
- `/v1/health/ping`: Simple ping endpoint

### Logging

Logs are written to:
- Console output
- Log file at `${APP_LOG_DIR}/wallet-server.log` (if configured)

### Recommended Monitoring Tools

- Prometheus for metrics collection
- Grafana for visualization
- ELK stack or similar for log aggregation

## Security Recommendations

- Deploy behind a reverse proxy/API gateway (e.g., Nginx, AWS API Gateway)
- Use HTTPS in production with valid SSL certificates
- Set restrictive CORS policies via `APP_SECURITY_ALLOWORIGINS`
- Use network security groups or firewalls to restrict access
- Consider implementing rate limiting at the API gateway level

## Backup and Disaster Recovery

The server is stateless and doesn't store critical data, so traditional backups are not required. However, ensure:

1. Configuration is version controlled
2. Deployment processes are automated and repeatable
3. Infrastructure is defined as code (for cloud deployments)

## Deployment Checklist

- [ ] Select appropriate deployment method (JAR, Docker, Kubernetes)
- [ ] Configure environment variables for the target environment
- [ ] Set up proper monitoring and logging
- [ ] Configure security settings (HTTPS, CORS, network policies)
- [ ] Test the deployment with health checks
- [ ] Set up auto-scaling rules if applicable
- [ ] Document the deployment process
- [ ] Create runbooks for common operational tasks
