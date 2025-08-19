# Troubleshooting

This guide provides solutions to common issues you might encounter when running or deploying the Wallet Server.

## Table of Contents

- [Connection Issues](#connection-issues)
- [API Response Errors](#api-response-errors)
- [Deployment Problems](#deployment-problems)
- [Configuration Issues](#configuration-issues)
- [Performance Problems](#performance-problems)
- [Transaction Broadcasting Failures](#transaction-broadcasting-failures)
- [Logging and Debugging](#logging-and-debugging)
- [Docker-Specific Issues](#docker-specific-issues)
- [Common Error Messages](#common-error-messages)

## Connection Issues

### Cannot Connect to Ethereum RPC

**Symptoms**: APIs that interact with Ethereum return connection errors or timeouts.

**Possible Causes**:
- Invalid RPC URL in configuration
- Network connectivity issues
- RPC provider is down or experiencing issues
- Rate limiting from the RPC provider

**Solutions**:
1. Verify the RPC URL is correct in your configuration
   ```bash
   # Check the configured URL
   curl -X GET http://localhost:8080/v1/eth/sepolia/config/rpc
   ```
2. Test direct connectivity to the RPC endpoint
   ```bash
   # Test direct connection to the RPC endpoint
   curl -X POST -H "Content-Type: application/json" --data '{"jsonrpc":"2.0","method":"eth_blockNumber","params":[],"id":1}' <YOUR_RPC_URL>
   ```
3. Check if your IP is allowlisted if using a private RPC endpoint
4. Configure fallback RPC providers in production environments

### Cannot Connect to Blockscout API

**Symptoms**: Transaction history endpoints return errors or empty responses.

**Possible Causes**:
- Invalid Blockscout API URL in configuration
- Blockscout API service is down
- Rate limiting from the Blockscout API

**Solutions**:
1. Verify the Blockscout URL is correct
2. Test direct connectivity to the Blockscout API
   ```bash
   # Test direct connection to Blockscout
   curl -X GET "<BLOCKSCOUT_URL>/<ADDRESS>/transactions"
   ```
3. Check Blockscout's status page for any reported outages

## API Response Errors

### 400 Bad Request

**Symptoms**: API returns 400 status code.

**Possible Causes**:
- Invalid Ethereum address format
- Invalid transaction hash format
- Missing required parameters
- Malformed JSON in request body

**Solutions**:
1. Ensure Ethereum addresses include the `0x` prefix and are 42 characters long
2. Verify transaction hashes include the `0x` prefix and are 66 characters long
3. Check that all required parameters are provided
4. Validate JSON syntax in request bodies

### 404 Not Found

**Symptoms**: API returns 404 status code.

**Possible Causes**:
- Incorrect API endpoint path
- Using a network name that isn't configured
- Looking up a transaction that doesn't exist

**Solutions**:
1. Double-check the API endpoint URL against the documentation
2. Verify that the network name is supported and properly configured
3. Confirm that the transaction hash exists on the blockchain

### 500 Internal Server Error

**Symptoms**: API returns 500 status code.

**Possible Causes**:
- Unexpected error in the application
- Issue with an external service
- Configuration problem
- Memory or resource constraints

**Solutions**:
1. Check application logs for detailed error information
   ```bash
   # View the last 100 lines of the log
   tail -100 /var/log/wallet-server/wallet-server.log
   ```
2. Verify that all required external services are available
3. Review application configuration for errors
4. Check system resources (CPU, memory) to ensure they're not exhausted

## Deployment Problems

### Application Fails to Start

**Symptoms**: The application doesn't start or crashes immediately after starting.

**Possible Causes**:
- Missing required configuration
- Port conflicts
- Insufficient permissions
- Java version mismatch

**Solutions**:
1. Check application logs for startup errors
2. Ensure all required environment variables or configuration properties are set
3. Verify the application port is available
   ```bash
   # Check if port 8080 is already in use
   lsof -i :8080
   ```
4. Verify the application has necessary permissions for file access
5. Confirm you're using Java 21 or newer
   ```bash
   java --version
   ```

### Cannot Connect to Deployed Service

**Symptoms**: The application is running but cannot be accessed remotely.

**Possible Causes**:
- Firewall blocking the port
- Network security group rules
- Container port not exposed
- Application binding to localhost only

**Solutions**:
1. Check firewall rules to ensure the application port is allowed
2. Verify network security group settings in cloud environments
3. Ensure container port mapping is correct if using Docker
   ```bash
   # Correct Docker port mapping
   docker run -p 8080:8080 wallet-server:latest
   ```
4. Check that the application is binding to all interfaces (0.0.0.0) not just localhost

## Configuration Issues

### Missing Configuration Properties

**Symptoms**: Application fails to start with configuration errors.

**Possible Causes**:
- Required properties not set
- Profile-specific properties missing
- Environment variables not correctly named

**Solutions**:
1. Review the logs for specific missing properties
2. Check that all required properties are defined in `application.yml` or environment variables
3. Verify environment variable naming (uppercase, underscores)
   ```bash
   # Example of correct environment variable format
   export APP_RPC_ETH_SEPOLIA="https://eth-sepolia.provider.com"
   ```
4. Ensure the correct Spring profile is active

### Environment Variables Not Applied

**Symptoms**: Configuration changes via environment variables don't take effect.

**Possible Causes**:
- Incorrect variable naming
- Environment variables set after application start
- Case sensitivity issues

**Solutions**:
1. Verify variable names match the expected format
2. Restart the application after setting environment variables
3. Double-check case sensitivity in variable names
4. Test environment variable override with a simple property

## Performance Problems

### Slow API Responses

**Symptoms**: API endpoints respond slowly.

**Possible Causes**:
- Slow RPC provider responses
- Insufficient resources allocated to the application
- Suboptimal JVM settings
- High load or concurrent requests

**Solutions**:
1. Monitor response times from RPC providers
2. Increase memory or CPU allocation for the application
3. Optimize JVM settings for your environment
   ```bash
   # Example of optimized JVM settings
   java -Xms512m -Xmx1g -XX:+UseG1GC -jar wallet-server-0.0.1-SNAPSHOT.jar
   ```
4. Consider implementing caching for frequently accessed data
5. Scale horizontally by deploying multiple instances behind a load balancer

### Memory Leaks

**Symptoms**: Application memory usage grows over time and performance degrades.

**Possible Causes**:
- Unclosed resources (connections, streams)
- Improper handling of large responses
- Caching without size limits

**Solutions**:
1. Use tools like JVisualVM or Java Mission Control to analyze memory usage
2. Ensure all resources are properly closed in `finally` blocks or try-with-resources
3. Review code for object retention issues
4. Consider implementing a memory leak detection tool

## Transaction Broadcasting Failures

### Transaction Fails to Broadcast

**Symptoms**: `sendTransaction` endpoint returns an error or transaction never appears on-chain.

**Possible Causes**:
- Invalid signed transaction data
- Nonce issues (too high, too low, already used)
- Insufficient gas price
- RPC node issues
- Network congestion

**Solutions**:
1. Verify that the signed transaction is correctly formatted
2. Check the nonce value using the `/eth/{network}/{address}/nonce` endpoint
3. Ensure gas price is sufficient using the `/eth/{network}/gas-fees` endpoint
4. Try a different RPC provider
5. Check blockchain explorers to see if the transaction was actually broadcast

### Invalid Transaction Signature

**Symptoms**: Transaction broadcast returns "invalid sender" or signature-related errors.

**Possible Causes**:
- Incorrectly signed transaction
- Wrong chain ID used during signing
- Transaction data corruption during transmission

**Solutions**:
1. Verify the transaction was signed with the correct private key
2. Ensure the chain ID matches the target network
3. Check that the transaction data wasn't modified after signing
4. Verify the signature algorithm is compatible with the network

## Logging and Debugging

### Insufficient Log Information

**Symptoms**: Unable to diagnose issues due to limited log information.

**Possible Causes**:
- Log level set too high
- Logs not being written to the expected location
- Logger not configured properly

**Solutions**:
1. Set more verbose logging levels in `application.yml`
   ```yaml
   logging:
     level:
       root: INFO
       com.example.wallet: DEBUG
   ```
2. Verify log file location and permissions
3. Check that logback configuration is correctly loaded

### Finding Specific Log Events

To search for specific events in logs:

```bash
# Find all error messages
grep "ERROR" /var/log/wallet-server/wallet-server.log

# Find logs related to a specific transaction
grep "0x1234567890abcdef" /var/log/wallet-server/wallet-server.log

# Find logs within a time range
grep "2025-08-19T10:" /var/log/wallet-server/wallet-server.log
```

## Docker-Specific Issues

### Container Stops Immediately

**Symptoms**: Docker container exits immediately after starting.

**Possible Causes**:
- Application crash on startup
- Missing environment variables
- Volume mount issues
- Permission problems

**Solutions**:
1. Run the container with attached console to see logs
   ```bash
   docker run -it wallet-server:latest
   ```
2. Verify all required environment variables are provided
3. Check volume mount paths and permissions
4. Ensure the Docker image is built correctly

### Cannot Access Container Services

**Symptoms**: Application running in Docker is not accessible.

**Possible Causes**:
- Port not exposed
- Incorrect port mapping
- Container network issues
- Application binding to localhost inside container

**Solutions**:
1. Ensure ports are correctly mapped
   ```bash
   # Correct port mapping
   docker run -p 8080:8080 wallet-server:latest
   ```
2. Verify the application is binding to 0.0.0.0 not localhost
3. Check Docker network settings
4. Use `docker exec` to verify the application is running inside the container
   ```bash
   docker exec -it <container_id> curl http://localhost:8080/v1/health/ping
   ```

## Common Error Messages

### "Unsupported ETH network"

**Cause**: The specified network name is not configured in the application.

**Solution**: 
1. Use a supported network name (e.g., "mainnet", "sepolia")
2. Add configuration for the desired network in `application.yml`
   ```yaml
   app:
     rpc:
       eth:
         your-network: "https://your-network-rpc.example"
   ```

### "Failed to fetch ETH balance"

**Cause**: Unable to retrieve balance from the Ethereum node.

**Solution**:
1. Verify the RPC URL is correct and accessible
2. Check that the address is valid
3. Ensure the Ethereum node is operational
4. Look for more specific error details in the application logs

### "Transaction failed: nonce too low"

**Cause**: The transaction was submitted with a nonce that has already been used.

**Solution**:
1. Use the `/eth/{network}/{address}/nonce` endpoint to get the current nonce
2. Increment the nonce value for each transaction
3. Wait for pending transactions to be mined before submitting new ones

### "Transaction underpriced"

**Cause**: The gas price for the transaction is too low for the current network conditions.

**Solution**:
1. Use the `/eth/{network}/gas-fees` endpoint to get current gas price recommendations
2. Increase the gas price when creating and signing the transaction
3. Consider using the "fast" or "fastest" gas price suggestion for quicker confirmation

## Getting Additional Help

If you're still experiencing issues after trying these troubleshooting steps:

1. Check the project's GitHub issues page for similar problems and solutions
2. Review the complete documentation for any relevant information
3. Create a detailed issue report including:
   - Application logs
   - Environment details (OS, Java version, etc.)
   - Steps to reproduce the issue
   - Expected vs. actual behavior
