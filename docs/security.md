# Security

This document outlines the security considerations, best practices, and implementation details for the Wallet Server application.

## Security Overview

Wallet Server is designed with security as a priority, especially given its role in blockchain and cryptocurrency operations. This document covers key security aspects to be aware of when deploying and using the application.

## Key Security Features

### Client-Side Transaction Signing

One of the most important security features is that the server **never handles private keys**. All transaction signing happens on the client side, and the server only broadcasts already signed transactions. This design principle significantly reduces the security risks associated with handling private keys.

```java
// EthTransferRequest only accepts already signed transactions
public class EthTransferRequest {
    @NotBlank(message = "Signed transaction data cannot be empty")
    @JsonProperty("signed_transaction")
    private String signedTransaction;
    
    // Metadata fields (optional, for information only)
    private String from;
    private String to;
    private String value;
    // ...
}
```

### Input Validation

The application implements thorough input validation using Jakarta Bean Validation:

- Request parameters are validated for format and content
- Path variables are checked for validity
- Request bodies are validated against schema definitions

```java
@GetMapping("/{address}/balance")
public ResponseEntity<BalanceResponse> getBalance(
        @PathVariable String network,
        @PathVariable @NotBlank String address) {
    // Address validation happens through @NotBlank annotation
    return ResponseEntity.ok(balanceService.ethBalance(network, address));
}
```

### Error Handling

A global exception handler ensures that detailed error information isn't leaked to clients in production:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        // Production-safe error response with limited details
        Map<String, Object> body = new HashMap<>();
        body.put("error", "An error occurred");
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("timestamp", new Date());
        body.put("path", request.getDescription(false));
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // Specific handlers for different exception types
    // ...
}
```

### CORS Configuration

Cross-Origin Resource Sharing (CORS) is configured to restrict which origins can access the API:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AppProperties appProperties;

    public WebConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(appProperties.getSecurity().getAllowOrigins().split(","))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
```

## Potential Security Risks and Mitigations

### RPC Node Security

**Risk**: The application connects to external RPC nodes which could be compromised or unreliable.

**Mitigation**:
- Use reputable RPC providers
- Consider running your own nodes for production
- Implement fallback mechanisms to alternate providers
- Validate responses from RPC nodes when possible

### Denial of Service (DoS)

**Risk**: The API could be targeted with excessive requests, potentially affecting service availability.

**Mitigation**:
- Implement rate limiting at the API gateway level
- Consider adding request throttling in high-traffic scenarios
- Monitor for unusual traffic patterns
- Deploy behind a CDN or DoS protection service like Cloudflare

### Man-in-the-Middle Attacks

**Risk**: Communication between clients and the API could be intercepted.

**Mitigation**:
- Always use HTTPS in production
- Implement certificate pinning for mobile clients
- Keep TLS configurations up to date with current best practices

### JSON Injection

**Risk**: Malformed JSON requests could potentially cause unexpected behavior.

**Mitigation**:
- Use strict JSON parsing with proper error handling
- Validate all incoming JSON against expected schemas
- Use content-type validation to ensure only valid JSON is accepted

### Frontend Security Considerations

If you're building a frontend application that uses this API:

- Never store private keys in browser storage
- Use a secure wallet connection method (e.g., WalletConnect)
- Implement proper CSP (Content Security Policy) headers
- Consider using Subresource Integrity (SRI) for external scripts

## Security Hardening for Production

When deploying Wallet Server in production, consider the following security enhancements:

### API Gateway Integration

Place the API behind a secure API gateway that provides:
- Rate limiting
- Request validation
- Authentication and authorization
- Request/response logging
- IP filtering if needed

### Network Security

- Deploy in a private subnet with limited access
- Use security groups or firewall rules to restrict traffic
- Only expose necessary ports (typically just 8080 for HTTP or 8443 for HTTPS)

### Containerization Security

If deploying with Docker:
- Use minimal base images (e.g., Eclipse Temurin JRE Alpine)
- Run containers as non-root users
- Scan images for vulnerabilities before deployment
- Use read-only file systems where possible

Example secure Dockerfile:
```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/wallet-server-0.0.1-SNAPSHOT.jar app.jar
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

### Logging and Monitoring

- Enable security-relevant logging
- Implement centralized log collection and analysis
- Set up alerts for suspicious activity
- Consider implementing audit logging for sensitive operations

## Security Best Practices

1. **Keep Dependencies Updated**: Regularly update dependencies to address security vulnerabilities
2. **Secure Configuration**: Never commit sensitive configuration to source control
3. **Principle of Least Privilege**: Services should only have the minimum permissions needed
4. **Security Headers**: Implement security headers like CSP, X-XSS-Protection, etc.
5. **Regular Security Reviews**: Conduct periodic security reviews of the codebase and infrastructure

## Responsible Disclosure

If you discover a security vulnerability, please report it responsibly by:

1. Not disclosing it publicly until it's fixed
2. Providing detailed information to the maintainers
3. Allowing reasonable time for the issue to be addressed before disclosure

## Security Testing

Consider implementing the following security testing approaches:

1. **Static Application Security Testing (SAST)**: Analyze source code for security vulnerabilities
2. **Dynamic Application Security Testing (DAST)**: Test running applications for exploitable vulnerabilities
3. **Dependency Scanning**: Check for known vulnerabilities in dependencies
4. **Penetration Testing**: Conduct simulated attacks to identify security weaknesses

## Conclusion

While Wallet Server implements several security best practices, no system is completely secure. Always follow defense-in-depth principles and regularly review and update security measures as threats evolve.
