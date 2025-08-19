# Development Guide

This guide provides detailed information for developers who want to contribute to or extend the Wallet Server application.

## Development Environment Setup

### Prerequisites

- JDK 21 or higher
- Maven 3.6+ or use the included Maven wrapper
- Git
- An IDE with Spring Boot support (recommended: IntelliJ IDEA, Eclipse, or VS Code with Java extensions)
- Docker (optional, for containerized testing)
- Postman or similar tool for API testing

### Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/smallyunet/wallet-server.git
   cd wallet-server
   ```

2. Import the project into your IDE as a Maven project.

3. Install dependencies:
   ```bash
   mvn clean install
   ```

4. Run the application in development mode:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

## Project Structure

The project follows a standard Spring Boot application structure:

```
wallet-server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── wallet/
│   │   │               ├── config/       # Configuration classes
│   │   │               ├── domain/       # Domain models and DTOs
│   │   │               ├── exception/    # Exception handlers
│   │   │               ├── infra/        # Infrastructure components
│   │   │               ├── service/      # Business services
│   │   │               ├── web/          # Web controllers
│   │   │               └── WalletServerApplication.java  # Main class
│   │   └── resources/
│   │       ├── application.yml           # Base configuration
│   │       ├── application-dev.yml       # Dev configuration
│   │       ├── application-prod.yml      # Production configuration
│   │       └── logback-spring.xml        # Logging configuration
│   └── test/                             # Test classes
├── docs/                                 # Documentation
├── logs/                                 # Log files
├── target/                               # Build output
├── pom.xml                               # Maven configuration
├── README.md                             # Project README
└── Dockerfile                            # Docker configuration
```

## Key Components

### Controllers

Controllers handle HTTP requests and are located in the `com.example.wallet.web` package:

- `EthController`: Handles Ethereum-related endpoints
- `BtcController`: Handles Bitcoin-related endpoints
- `HealthController`: Provides health check endpoints

### Services

Services implement business logic and are located in the `com.example.wallet.service` package:

- `BalanceService`: Handles balance and transaction-related operations
- `BlockscoutService`: Manages interaction with Blockscout API

### Infrastructure

Infrastructure components provide integration with external services and are located in the `com.example.wallet.infra` package:

- `eth.EthClient`: Interacts with Ethereum nodes through Web3j
- `btc.BtcClient`: Interacts with Bitcoin API endpoints
- `eth.BlockscoutProvider`: Interfaces with the Blockscout API

### Domain Models

Domain models represent the data structures used in the application and are located in the `com.example.wallet.domain` package:

- `BalanceResponse`: Generic balance response model
- `eth.*`: Ethereum-specific models
- `blockscout.*`: Models for Blockscout API integration

## Development Workflow

### Adding a New API Endpoint

1. Define any new domain models needed in the appropriate package.
2. Implement the necessary business logic in the service layer.
3. Create a new controller method to handle the HTTP request.
4. Add validation for request parameters.
5. Write tests for the new functionality.
6. Document the API endpoint in the API reference.

Example of adding a new endpoint:

```java
// 1. Define a new response model (if needed)
public class NewFeatureResponse {
    private String field1;
    private int field2;
    
    // Getters and setters
}

// 2. Add method to service
@Service
public class ExistingService {
    // Existing code...
    
    public NewFeatureResponse newFeature(String param1, int param2) {
        // Business logic
        return new NewFeatureResponse();
    }
}

// 3. Add controller method
@RestController
@RequestMapping("/v1/eth/{network}")
public class EthController {
    // Existing code...
    
    @GetMapping("/new-feature")
    public ResponseEntity<NewFeatureResponse> newFeature(
            @PathVariable String network,
            @RequestParam @NotBlank String param1,
            @RequestParam int param2) {
        return ResponseEntity.ok(existingService.newFeature(param1, param2));
    }
}
```

### Adding Support for a New Blockchain

1. Create new domain models specific to the blockchain.
2. Implement a new client class in the infrastructure layer.
3. Add necessary service methods.
4. Create a new controller for the blockchain.
5. Update configuration properties to include the new blockchain's RPC endpoints.
6. Add documentation for the new blockchain support.

### Error Handling

The application uses a global exception handler defined in `GlobalExceptionHandler.java`. To add handling for a new exception type:

1. Create a custom exception class if needed.
2. Add a new method to the exception handler:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    // Existing code...
    
    @ExceptionHandler(YourCustomException.class)
    public ResponseEntity<Object> handleYourCustomException(
            YourCustomException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", new Date());
        body.put("path", request.getDescription(false));
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
```

## Testing

### Unit Testing

Unit tests should be created for all service and infrastructure components. Use Spring's testing framework and Mockito for mocking dependencies.

Example unit test:

```java
@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {
    @Mock
    private EthClient ethClient;
    
    @InjectMocks
    private BalanceService balanceService;
    
    @Test
    public void testEthBalance() {
        // Arrange
        String network = "sepolia";
        String address = "0x1234...";
        String expectedBalance = "0x100";
        when(ethClient.getBalance(network, address)).thenReturn(expectedBalance);
        
        // Act
        BalanceResponse response = balanceService.ethBalance(network, address);
        
        // Assert
        assertEquals("ETH", response.getCoin());
        assertEquals(network, response.getNetwork());
        assertEquals(address, response.getAddress());
        assertEquals(expectedBalance, response.getBalance());
    }
}
```

### Integration Testing

Integration tests should test the interaction between components and with external services. Use Spring Boot's testing utilities for this.

### API Testing

API tests should verify that the API endpoints work as expected. Use Spring Boot's `@WebMvcTest` for controller testing or tools like RestAssured for full API testing.

Example API test:

```java
@WebMvcTest(EthController.class)
public class EthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private BalanceService balanceService;
    
    @Test
    public void testGetBalance() throws Exception {
        // Arrange
        String network = "sepolia";
        String address = "0x1234...";
        BalanceResponse expectedResponse = new BalanceResponse("ETH", network, address, "0x100");
        when(balanceService.ethBalance(network, address)).thenReturn(expectedResponse);
        
        // Act & Assert
        mockMvc.perform(get("/v1/eth/{network}/{address}/balance", network, address))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.coin").value("ETH"))
               .andExpect(jsonPath("$.network").value(network))
               .andExpect(jsonPath("$.address").value(address))
               .andExpect(jsonPath("$.balance").value("0x100"));
    }
}
```

## Logging

The application uses SLF4J with Logback for logging. Log statements should be meaningful and at the appropriate level:

- ERROR: For errors that affect functionality and require attention
- WARN: For potentially harmful situations that don't break functionality
- INFO: For major lifecycle events and important business operations
- DEBUG: For detailed information useful during development and debugging
- TRACE: For very fine-grained debugging information

Example logging usage:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YourService {
    private static final Logger logger = LoggerFactory.getLogger(YourService.class);
    
    public void someMethod() {
        logger.info("Processing request for user {}", userId);
        
        try {
            // Some operation
            logger.debug("Operation details: {}", details);
        } catch (Exception e) {
            logger.error("Failed to process request", e);
            throw e;
        }
    }
}
```

## Building for Production

To build the application for production:

```bash
mvn clean package -DskipTests
```

This will create a JAR file in the `target` directory that can be deployed to a production environment.

## Docker Development

You can use Docker for development and testing:

```bash
# Build the Docker image
docker build -t wallet-server:dev .

# Run the container
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev wallet-server:dev
```

## Coding Standards

The project follows standard Java coding conventions:

- Use camelCase for variables and methods
- Use PascalCase for classes
- Use UPPER_SNAKE_CASE for constants
- Use 4-space indentation
- Maximum line length of 120 characters
- Document public APIs with JavaDoc comments

## Code Review Process

When submitting code for review:

1. Ensure all tests pass
2. Verify code quality using static analysis tools
3. Include meaningful commit messages
4. Update documentation as needed
5. Add test coverage for new functionality
6. Follow the project's coding standards

## Continuous Integration

The project uses GitHub Actions for continuous integration, which runs the following checks on each push and pull request:

- Compile the code
- Run unit and integration tests
- Check code quality
- Build the Docker image

## Versioning

The project follows [Semantic Versioning](https://semver.org/):

- MAJOR version for incompatible API changes
- MINOR version for backwards-compatible functionality additions
- PATCH version for backwards-compatible bug fixes

## Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Web3j Documentation](https://docs.web3j.io/)
- [Maven Documentation](https://maven.apache.org/guides/)
