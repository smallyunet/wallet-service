#!/bin/bash

# Run all unit tests and generate test coverage report

echo "==============================================="
echo "Running Wallet Server Unit Tests with Coverage"
echo "==============================================="

# Clean previous reports
echo "Cleaning previous test reports..."
mvn clean

# Run tests and generate report
echo "Running tests..."
mvn test

# Check test results
if [ $? -eq 0 ]; then
  echo "==============================================="
  echo "✅ All tests passed successfully!"
  echo "==============================================="
else
  echo "==============================================="
  echo "❌ Some tests failed. Check the report for details."
  echo "==============================================="
fi

echo "Test report available at: target/surefire-reports"
