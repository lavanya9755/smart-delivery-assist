from flask import Flask, request, jsonify
from flask_cors import CORS
from textblob import TextBlob
import joblib
import numpy as np
from sklearn.preprocessing import StandardScaler
import os

app = Flask(__name__)
CORS(app)

# Load models (we'll train these later)
model_dir = 'models'
if not os.path.exists(model_dir):
    os.makedirs(model_dir)

def load_model(name):
    try:
        return joblib.load(f'{model_dir}/{name}.joblib')
    except:
        return None

eta_model = load_model('eta_predictor')
fraud_model = load_model('fraud_detector')
cluster_model = load_model('customer_segmenter')
scaler = load_model('scaler')

@app.route('/predict_eta', methods=['POST'])
def predict_eta():
    try:
        data = request.json
        features = np.array([[
            data['distance'],
            data['weight'],
            data['traffic_level']
        ]])
        
        if eta_model is None:
            # Return dummy prediction if model not trained
            return jsonify({'eta_minutes': 30.0})
            
        scaled_features = scaler.transform(features)
        prediction = eta_model.predict(scaled_features)[0]
        return jsonify({'eta_minutes': float(prediction)})
    except Exception as e:
        return jsonify({'error': str(e)}), 400

@app.route('/detect_fraud', methods=['POST'])
def detect_fraud():
    try:
        data = request.json
        features = np.array([[
            data['delivery_frequency'],
            data['total_amount'],
            data['city_changes']
        ]])
        
        if fraud_model is None:
            # Return dummy prediction if model not trained
            return jsonify({'is_fraudulent': False})
            
        scaled_features = scaler.transform(features)
        prediction = fraud_model.predict(features)[0]
        return jsonify({'is_fraudulent': bool(prediction)})
    except Exception as e:
        return jsonify({'error': str(e)}), 400

@app.route('/cluster_user', methods=['POST'])
def cluster_user():
    try:
        data = request.json
        features = np.array([[
            data['deliveries_per_month'],
            data['avg_rating'],
            data['refund_count']
        ]])
        
        if cluster_model is None:
            # Return dummy prediction if model not trained
            return jsonify({'cluster': 0})
            
        scaled_features = scaler.transform(features)
        cluster = cluster_model.predict(scaled_features)[0]
        return jsonify({'cluster': int(cluster)})
    except Exception as e:
        return jsonify({'error': str(e)}), 400

@app.route('/analyze_sentiment', methods=['POST'])
def analyze_sentiment():
    try:
        text = request.json['text']
        analysis = TextBlob(text)
        sentiment_score = analysis.sentiment.polarity
        
        # Map the sentiment score to a label
        if sentiment_score > 0.1:
            sentiment = 'positive'
        elif sentiment_score < -0.1:
            sentiment = 'negative'
        else:
            sentiment = 'neutral'
            
        return jsonify({
            'sentiment': sentiment,
            'score': sentiment_score
        })
    except Exception as e:
        return jsonify({'error': str(e)}), 400

if __name__ == '__main__':
    app.run(port=5000, debug=True) 