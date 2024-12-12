# Emas and Saham Price Prediction API

## Introduction

This repository contains the documentation and implementation for the Emas and Saham Price Prediction API, which allows users to predict gold and stock prices based on machine learning models.

### Notes:
- The data types for **gold prediction** include `income` (float) and `expenses` (float).
- The data types for **stock prediction** include `ticker` (string) for stock symbols.
- The predictions provide insights into future trends based on historical data and AI models.
- Ensure all required fields are filled when making API requests.

---

## Gold Price Prediction

### **POST** `/predict/gold`

**Request JSON:**
- `income`: (float) The user's monthly income.
- `expenses`: (float) The user's monthly expenses.

**Example Request:**
```json
{
  "income": 5000,
  "expenses": 3000
}
```

**Response Success:**
- **Message**: "Gold prediction success"
- **Data**:
  - `predicted_prices`: A list of predicted gold prices for the next 200 days.
  - `recommendation`: A recommendation based on the predicted gold prices ("BUY" or "WAIT").

**Example Response:**
```json
{
  "predicted_prices": [1800.5, 1810.7, ..., 1850.2],
  "recommendation": "BUY"
}
```

**Response Error:**
- **Message**: "Server Error"
- **Server Message**: "Gold prediction error: <error_message>"

---

## Stock Price Prediction

### **GET** `/predict/stock/{ticker}`

**Parameters:**
- `ticker`: (string) The stock ticker symbol (e.g., `BBCA`, `ACES`, etc.).

**Example Request:**
`http://localhost:8080/predict/stock/BBCA`

**Response Success:**
- **Message**: "Stock prediction success"
- **Data**:
  - `predicted_price`: The predicted stock price for each period.
  - `date`: The corresponding prediction date (1 day, 1 month, 1 year).

**Example Response:**
```json
{
  "1_day_prediction_date": "2024-12-14 00:00:00",
  "1_month_prediction_date": "2025-01-13 00:00:00",
  "1_year_prediction_date": "2025-12-13 00:00:00",
  "1_day": {
    "predicted_price": 100.5,
    "date": "2024-12-14 00:00:00"
  },
  "1_month": {
    "predicted_price": 105.8,
    "date": "2025-01-13 00:00:00"
  },
  "1_year": {
    "predicted_price": 110.2,
    "date": "2025-12-13 00:00:00"
  }
}
```

**Response Error:**
- **Message**: "Server Error"
- **Server Message**: "Stock prediction error for {ticker} on {period}: {error_message}"

---

## Root Endpoint

### **GET** `/`

**Response Success:**
- **Message**: "Emas and Saham Price Prediction API is running"

**Example Response:**
```json
{
  "message": "Gold and Stock Price Prediction API is running"
}
```

---

## Error Handling

For all endpoints, the API responds with standard error messages in case of issues like missing files, invalid inputs, or server failures. The `HTTPException` is used to return detailed error messages.

**Example Error Response:**
```json
{
  "detail": "Invalid ticker: BBXX"
}
```

---

## How to Run

1. Clone this repository:
   ```bash
   git clone [https://github.com/yourusername/emas-saham-prediction-api.git](https://github.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/tree/main/Cloud%20Computing/API%20Emas%20dan%20Saham)
   ```

2. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```

3. Start the FastAPI server:
   ```bash
   uvicorn main:app --host 0.0.0.0 --port 8080
   ```

4. Access the API documentation at:
   - OpenAPI: `http://localhost:8080/predict`
   - ReDoc: `http://localhost:8080/predict`

---

## Contact

For any inquiries, please contact [m.haidaermuzaki21@gmail.com](m.haidaermuzaki21@gmail.com).
