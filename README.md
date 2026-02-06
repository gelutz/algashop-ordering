# Ordering Microservice

Customer and order management service for AlgaShop e-commerce platform.

## Overview

This microservice handles customer registration, management, and order processing using Domain-Driven Design principles.

## Features

- Customer registration and management
- Loyalty points system
- Promotion notification preferences
- Customer archiving with privacy protection
- Comprehensive validation and business rules

## Domain Model

### Customer Entity
The core domain entity with the following capabilities:
- **Registration**: Create new customers with validated data
- **Updates**: Modify customer information (name, email, phone)
- **Archiving**: Secure customer deletion with privacy protection
- **Loyalty Points**: Accumulate and manage reward points
- **Notifications**: Control promotional communication preferences

### Order Entity
Manages the complete order lifecycle from creation to fulfillment:
- **Draft Creation**: Orders start as drafts for customers to build their cart
- **Item Management**: Add products with automatic total calculation (items cost + shipping)
- **Placement**: Validates all required information (shipping info, billing info, payment method, delivery date, and items) before placement
- **Status Workflow**: Orders progress through states: DRAFT → PLACED → PAID → READY, with the ability to cancel from any state
- **Shipping Management**: Update shipping details with validation that delivery dates must be in the future
- **State Validation**: Prevents invalid status transitions (e.g., cannot place an already placed order)

### Value Objects
- **CustomerId**: Unique customer identifier
- **FullName**: Customer name with validation
- **Email**: Email address with format validation
- **Phone**: Phone number with validation
- **Document**: Document number with validation
- **Birthdate**: Date of birth with age validation
- **LoyaltyPoints**: Customer reward points

## API Endpoints

(Endpoints will be documented as they are implemented)

## Development

### Running Locally

```bash
./gradlew bootRun
```

### Testing

```bash
./gradlew test
```

### Building

```bash
./gradlew build
```

### Git Hooks Setup

This project uses a pre-commit hook that automatically runs tests before each commit to ensure code quality.

#### Setup Instructions

To configure git hooks for this project:

```bash
# Move the pre-commit hook to the project directory
mv .git/hooks/pre-commit .githooks/

# Configure git to use the local hooks directory
git config --local core.hooksPath .githooks/
```

The pre-commit hook runs `./gradlew test` and will abort the commit if tests fail.

## Architecture

- **Package**: `com.lutz.algashop.ordering`
- **Domain Layer**: Pure business logic without external dependencies
- **Application Layer**: Spring Boot application configuration
- **Validation**: Custom field validation framework
- **Exceptions**: Domain-specific exception hierarchy