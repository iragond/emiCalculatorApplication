# Loan EMI Calculator

A Spring Boot application to calculate the **Equated Monthly Installment (EMI)** for home, car, and personal loans.

It exposes a REST API that accepts loan details, validates the request using **Jakarta Validation**, and returns either the calculated EMI or a structured validation error response.

## Tech Stack / Dependencies
- Java 17
- Spring Boot 3
- Spring Web (embedded Tomcat)
- Jakarta Validation (`spring-boot-starter-validation`)
- Lombok
- Maven

## Inputs
- **Loan Amount** (`loanValue`)
- **Yearly Interest Rate** (`yearlyInterestRate`)
- **Loan Term in years** (`loanTermYears`)

## Features
- Validates **loan value**, **yearly interest rate**, and **loan term** using **Jakarta Validation**.
- Converts **yearly interest rate** to a **monthly interest rate** during calculation.
- Calculates EMI using the standard formula:

  `EMI = P * r * (1 + r)^n / ((1 + r)^n - 1)`


- Returns clear validation error messages in a consistent JSON format:
  - `message`: high-level error summary
  - `fieldErrors`: per-field validation messages
- Provides a RESTful API endpoint following clean coding and backend best practices.
## Clone repo using below URL:
https://github.com/iragond/emiCalculatorApplication.git
## Running the Project

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

## API

### POST `/api/v1/emi/calculate`

#### Request Body
```json
{
  "loanValue": 100000,
  "yearlyInterestRate": 10,
  "loanTermYears": 20
}
```

### Scenario 1: Success Response

Request:
```json
{
  "loanValue": 1,
  "yearlyInterestRate": 10,
  "loanTermYears": 20
}
```

Response:
```json
{
  "emiAmount": 0.009650216450740089,
  "message": "EMI calculated successfully."
}
```

### Scenario 2: Validation Failure Response

Request:
```json
{
  "loanValue": -20,
  "yearlyInterestRate": 10,
  "loanTermYears": 20
}
```

Response:
```json
{
  "message": "Validation failed",
  "fieldErrors": {
    "loanValue": "Loan value must be a positive number."
  }
}
```

## Quick Test (cURL)
```bash
curl -X POST http://localhost:8080/api/v1/emi/calculate \
  -H "Content-Type: application/json" \
  -d '{"loanValue":500000,"yearlyInterestRate":12,"loanTermYears":30}'
```
