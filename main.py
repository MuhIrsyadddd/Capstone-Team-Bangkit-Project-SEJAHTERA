from flask import Flask, request, jsonify
import numpy as np
import pandas as pd
from tensorflow.keras.models import load_model
from sklearn.preprocessing import MinMaxScaler
from datetime import datetime, timedelta
import os

# Initialize Flask app
app = Flask(__name__)

# Directory where your models are stored
MODEL_DIR = "models"  # Folder tempat semua file model .h5 disimpan
SCALER_DIR = "scalers"  # Tempat menyimpan scaler untuk setiap model

# Load dataset mapping (saham)
MODEL_DATASET_MAPPING = {
    "ACES": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/ACES.csv',
    "ADRO": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/ADRO.csv',
    "AKRA": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/AKRA.csv',
    "AMMN": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/AMMN.csv',
    "AMRT": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/AMRT.csv',
    "ANTM": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/ANTM.csv',
    "ARTO": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/ARTO.csv',
    "ASII": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/ASII.csv',
    "BBCA": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/BBCA.csv',
    "BBNI": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/BBNI.csv',
    "BBRI": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/BBRI.csv',
    "BBTN": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/BBTN.csv',
    "BMRI": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/BMRI.csv',
    "BRIS": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/BRIS.csv',
    "BRPT": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/BRPT.csv',
    "BUKA": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/BUKA.csv',
    "CPIN": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/CPIN.csv',
    "EMTK": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/EMTK.csv',
    "ESSA": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/ESSA.csv',
    "EXCL": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/EXCL.csv',
    "GGRM": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/GGRM.csv',
    "GOTO": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/GOTO.csv',
    "HRUM": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/HRRUM.csv',
    "ICBP": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/ICBP.csv',
    "INCO": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/INCO.csv',
    "INDF": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/INFG.csv',
    "INDY": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/INDY.csv',
    "INKP": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/INKP.csv',
    "INTP": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/INTP.csv',
    "ISAT": 'https://raw.githubusercontent.com/MuhIrsyadddd/Capstone-Team-Bangkit-Project-SEJAHTERA/main/Dataset/Saham%20New/ISAT.csv',
    # Tambahkan model lain sesuai kebutuhan
}

# Load model and scaler for gold
GOLD_MODEL_PATH = os.path.join(MODEL_DIR, "EMAS_model.h5")
gold_model = load_model(GOLD_MODEL_PATH)
gold_scaler = MinMaxScaler()

# Function to create sequences for gold data
def create_gold_sequences(data, seq_length):
    X = []
    for i in range(len(data) - seq_length):
        X.append(data[i:i + seq_length])
    return np.array(X)

# Endpoint untuk prediksi saham
@app.route('/predict', methods=['POST'])
def predict():
    """
    Predict stock prices for a specific model and number of days.
    Expects a JSON input: {"stock": "BBRI", "days": 30}
    """
    request_data = request.get_json()

    stock = request_data.get("stock", None)  # Nama saham (misalnya, BBRI atau ANTM)
    days = request_data.get("days", 30)  # Jumlah hari prediksi (default: 30)

    if stock not in MODEL_DATASET_MAPPING:
        return jsonify({"error": f"Model for stock '{stock}' not found"}), 400

    # Load the corresponding model
    model_path = os.path.join(MODEL_DIR, f"{stock}_model.h5")
    if not os.path.exists(model_path):
        return jsonify({"error": f"Model file for stock '{stock}' not found"}), 404

    model = load_model(model_path)

    # Load dataset and preprocess
    file_path = MODEL_DATASET_MAPPING[stock]
    data = pd.read_csv(file_path)
    data['date'] = pd.to_datetime(data['date'])
    data = data.sort_values(by='date')

    # Prepare scalers
    scaler_features = MinMaxScaler()
    scaler_target = MinMaxScaler()

    # Add features
    data['SMA_10'] = data['close'].rolling(window=10).mean()
    data['EMA_10'] = data['close'].ewm(span=10, adjust=False).mean()
    data['RSI_14'] = 100 - (100 / (1 + data['close'].diff().apply(lambda x: max(x, 0)).rolling(window=14).mean() /
                                   data['close'].diff().apply(lambda x: abs(x)).rolling(window=14).mean()))
    for lag in range(1, 6):
        data[f'close_lag_{lag}'] = data['close'].shift(lag)

    data_cleaned = data.dropna()

    # Prepare features
    features = ['close', 'SMA_10', 'EMA_10', 'RSI_14'] + [f'close_lag_{i}' for i in range(1, 6)]
    X_features = data_cleaned[features]
    y_target = data_cleaned[['close']]

    # Scale features and target
    X_scaled = scaler_features.fit_transform(X_features)
    y_scaled = scaler_target.fit_transform(y_target)

    # Use the last sequence from the scaled data for predictions
    last_sequence = X_scaled[-1].reshape(1, 1, X_scaled.shape[1])
    future_predictions = []

    for _ in range(days):
        next_pred = model.predict(last_sequence)
        future_predictions.append(next_pred[0, 0])
        last_sequence = np.roll(last_sequence, -1, axis=2)
        last_sequence[0, 0, -1] = next_pred[0, 0]

    # Rescale predictions back to the original scale
    future_predictions_rescaled = scaler_target.inverse_transform(
        np.array(future_predictions).reshape(-1, 1)
    )

    # Generate future dates
    last_date = data_cleaned['date'].iloc[-1]
    future_dates = [last_date + timedelta(days=i) for i in range(1, days + 1)]

    # Prepare the response
    response_data = {
        "stock": stock,
        "predictions": [
            {"date": date.strftime("%Y-%m-%d"), "predicted_close": float(price)}
            for date, price in zip(future_dates, future_predictions_rescaled.flatten())
        ]
    }

    return jsonify(response_data)

# Endpoint untuk prediksi emas
@app.route('/gold_predict', methods=['POST'])
def gold_predict():
    """
    Predict future gold prices based on historical data.
    Expects JSON input: {"income": 5000, "expenses": 3000, "days": 7}
    """
    request_data = request.get_json()

    income = request_data.get("income")
    expenses = request_data.get("expenses")
    days = request_data.get("days", 7)  # Default to 7 days

    if income is None or expenses is None:
        return jsonify({"error": "Missing income or expenses"}), 400

    # Calculate available funds
    available_funds = income - expenses
    if available_funds <= 0:
        return jsonify({"message": "No available funds for investment."})

    # Load dataset
    file_path = "path_to_gold_dataset.csv"  # Replace with actual path
    df = pd.read_csv(file_path)
    df['Price'] = df['Price'].str.replace(',', '').astype(float)
    df['Date'] = pd.to_datetime(df['Date'], format='%m/%d/%Y')
    df.set_index('Date', inplace=True)

    # Normalize data
    prices = gold_scaler.fit_transform(df[['Price']].values)

    # Create sequences
    seq_length = 30
    last_sequence = prices[-seq_length:]  # Last sequence for prediction
    future_prices = []

    for _ in range(days):
        prediction = gold_model.predict(last_sequence.reshape(1, seq_length, 1))
        future_prices.append(prediction[0, 0])
        last_sequence = np.append(last_sequence[1:], prediction).reshape(seq_length, 1)

    # Inverse transform predictions
    future_prices = gold_scaler.inverse_transform(np.array(future_prices).reshape(-1, 1))

    # Generate future dates
    last_date = df.index[-1]
    future_dates = [last_date + timedelta(days=i) for i in range(1, days + 1)]

    # Prepare response
    response = {
        "future_prices": [
            {"date": date.strftime("%Y-%m-%d"), "price": float(price)}
            for date, price in zip(future_dates, future_prices.flatten())
        ]
    }

    return jsonify(response)

# Run the Flask app
if __name__ == "__main__":
    app.run(debug=True)
