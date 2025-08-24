# Wallet Server Makefile

# Default target
.PHONY: all
all: build

# Build the project without running tests
.PHONY: build
build:
	mvn -DskipTests package

# Clean the project
.PHONY: clean
clean:
	mvn clean

# Run the project
.PHONY: run
run:
	mvn spring-boot:run

# Build and run the project
.PHONY: build-run
build-run: build run

# Run tests
.PHONY: test
test:
	mvn test

# Help target to show available commands
.PHONY: help
help:
	@echo "Available commands:"
	@echo "  make          - Build the project without running tests"
	@echo "  make build    - Build the project without running tests"
	@echo "  make clean    - Clean the project"
	@echo "  make run      - Run the project"
	@echo "  make test     - Run tests"
	@echo "  make build-run - Build and run the project"
	@echo "  make help     - Show this help message"
