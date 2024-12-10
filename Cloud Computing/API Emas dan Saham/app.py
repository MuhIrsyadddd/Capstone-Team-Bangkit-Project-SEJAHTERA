    from fastapi import FastAPI, HTTPException
    from tensorflow.keras.models import load_model
    from tensorflow.keras import metrics
    from tensorflow.keras.saving import register_keras_serializable
    import numpy as np
    import joblib
    import os
    import pickle
    import logging
    from datetime import datetime, timedelta
    import pytz
    from pydantic import BaseModel

    # Initialize FastAPI app
    app = FastAPI()

    # Logging configuration
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger(__name__)

    # Directory settings
    MODEL_DIR = "models"
    SCALER_DIR = "scalers"

    # Load gold model and scaler
    try:
        gold_model_path = os.path.join(MODEL_DIR, "EMAS_model.h5")
        gold_scaler_path = os.path.join(SCALER_DIR, "EMAS_scaler.pkl")

        gold_model = load_model(gold_model_path)
        with open(gold_scaler_path, "rb") as f:
            gold_scaler = pickle.load(f)
    except Exception as e:
        logger.error(f"Failed to load gold model or scaler: {e}")
        gold_model = None
        gold_scaler = None

    # Ticker list
    TICKERS = [
        "ACES", "ADRO", "AKRA", "AMMN", "AMRT", "ANTM", "ARTO", "ASII",
        "BBCA", "BBNI", "BBRI", "BBTN", "BMRI", "BRIS", "BRPT", "BUKA",
        "CPIN", "EMTK", "ESSA", "EXCL", "GGRM", "GOTO", "HRUM", "ICBP",
        "INCO", "INDF", "INDY", "INKP", "INTP", "ISAT"
    ]



    # Helper functions
    def preprocess_gold_data(data):
        return gold_scaler.transform(data)

    def predict_gold_prices(income, expenses, days=200, seq_length=30):
        available_funds = income - expenses
        if available_funds <= 0:
            return {
                "message": "No available funds for investment.",
                "recommendation": "WAIT"
            }

        historical_data = np.zeros((seq_length, 1))
        try:
            historical_data = np.load("last_sequence.npy")
        except FileNotFoundError:
            pass

        future_prices = []
        for _ in range(days):  # Run for 200 days
            prediction = gold_model.predict(historical_data.reshape(1, seq_length, 1))
            future_prices.append(prediction[0, 0])
            historical_data = np.append(historical_data[1:], prediction).reshape(seq_length, 1)

        future_prices = gold_scaler.inverse_transform(np.array(future_prices).reshape(-1, 1))

        recommendation = "BUY" if future_prices[-1] > future_prices[0] else "WAIT"

        return {
            "predicted_prices": future_prices.flatten().tolist(),  # Return 200 predictions
            "recommendation": recommendation
        }

    def get_prediction_dates():
        today = datetime.now(pytz.timezone("Asia/Jakarta"))
        return {
            "1_day_prediction_date": (today + timedelta(days=1)).strftime('%Y-%m-%d %H:%M:%S'),
            "1_month_prediction_date": (today + timedelta(days=30)).strftime('%Y-%m-%d %H:%M:%S'),
            "1_year_prediction_date": (today + timedelta(days=365)).strftime('%Y-%m-%d %H:%M:%S')
        }

    def load_scaler(ticker):
        scaler_path = os.path.join(SCALER_DIR, f"{ticker}_features_scaler.pkl")
        if not os.path.exists(scaler_path):
            raise FileNotFoundError(f"Scaler file not found: {scaler_path}")
        return joblib.load(scaler_path)

    @register_keras_serializable()
    def mse(y_true, y_pred):
        return metrics.mean_squared_error(y_true, y_pred)

    def predict_stock_price(ticker):
        model_path = os.path.join(MODEL_DIR, f"{ticker}_model.h5")
        if not os.path.exists(model_path):
            raise FileNotFoundError(f"Model file not found: {model_path}")

        scaler = load_scaler(ticker)
        model = load_model(model_path, custom_objects={"mse": mse})

        input_shape = model.input_shape[1:]
        dummy_features = np.random.rand(1, *input_shape)
        prediction = model.predict(dummy_features)
        return float(prediction[0][0])

    # Endpoint for gold prediction
    class GoldPredictionInput(BaseModel):
        income: float
        expenses: float

    @app.post("/predict/gold")
    async def predict_gold(input: GoldPredictionInput):
        try:
            result = predict_gold_prices(input.income, input.expenses, days=200)  # Default days=200
            return result
        except Exception as e:
            logger.error(f"Gold prediction error: {e}")
            raise HTTPException(status_code=500, detail="Internal Server Error")

    # Endpoint for stock prediction
    @app.get("/predict/stock/{ticker}")
    async def predict_stock(ticker: str):
        if ticker not in TICKERS:
            raise HTTPException(status_code=400, detail=f"Invalid ticker: {ticker}")

        prediction_dates = get_prediction_dates()
        predictions = {}

        for period, date in prediction_dates.items():
            try:
                predicted_price = predict_stock_price(ticker)
                predictions[period] = {
                    "predicted_price": predicted_price,
                    "date": date
                }
            except Exception as e:
                logger.error(f"Stock prediction error for {ticker} on {period}: {e}")
                predictions[period] = {"error": f"Prediction failed for {period}"}

        return predictions

    # Root endpoint
    @app.get("/")
    async def root():
        return {"message": "Emas and Saham Price Prediction API is running"}

    if __name__ == "__main__":
        import uvicorn
        uvicorn.run(app, host="0.0.0.0", port=8080)
