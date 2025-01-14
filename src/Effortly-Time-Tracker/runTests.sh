#!/bin/bash

echo "Running Unit Tests..."
mvn clean test -Punit-tests || exit 1

echo "Running Integration Tests..."
mvn  test -Pintegration-tests || exit 1

echo "Running E2E Tests..."
mvn  verify -Pe2e-tests || exit 1

echo "All tests completed successfully."
