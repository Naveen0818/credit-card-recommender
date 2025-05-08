# Credit Card Recommender System

A Spring Boot application that predicts credit categories and recommends credit cards based on user profiles.

## Features

- Credit category prediction (EXCELLENT, GOOD, FAIR, POOR)
- User-friendly input fields
- Machine learning model for predictions
- RESTful API endpoints

## Technology Stack

- Java 17
- Spring Boot
- Apache Commons Math (for regression)
- Maven

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Running the Application

1. Clone the repository:
```bash
git clone <repository-url>
cd credit-card-recommender
```

2. Build the project:
```bash
./mvnw clean install
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on port 8080.

### API Usage

#### Predict Credit Category

```bash
curl -X POST http://localhost:8080/api/v1/credit/predict \
  -H "Content-Type: application/json" \
  -d '{
    "annualIncome": 120000,
    "monthlyDebtPayments": 3000,
    "oldestAccountAge": 12,
    "activeCreditCards": 3,
    "totalLoans": 2,
    "creditCardUsage": 0.35,
    "onTimePayments": 11
  }'
```

## Project Structure

- `src/main/java/com/credit/model`: Data models
- `src/main/java/com/credit/service`: Business logic
- `src/main/java/com/credit/controller`: REST controllers
- `src/main/java/com/credit/util`: Utility classes
- `src/main/resources`: Configuration and training data

## License

This project is licensed under the MIT License. 