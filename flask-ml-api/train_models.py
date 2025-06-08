import numpy as np
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestClassifier
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler
import joblib
import os

# Create models directory if it doesn't exist
model_dir = 'models'
if not os.path.exists(model_dir):
    os.makedirs(model_dir)

# Generate dummy data
np.random.seed(42)

# ETA Prediction Data
n_samples = 1000
X_eta = np.random.rand(n_samples, 3)  # distance, weight, traffic_level
y_eta = 30 + 10 * X_eta[:, 0] + 5 * X_eta[:, 1] + 15 * X_eta[:, 2] + np.random.normal(0, 2, n_samples)

# Fraud Detection Data
X_fraud = np.random.rand(n_samples, 3)  # delivery_frequency, total_amount, city_changes
y_fraud = (X_fraud[:, 0] + X_fraud[:, 1] + X_fraud[:, 2] > 2).astype(int)

# Customer Segmentation Data
X_cluster = np.random.rand(n_samples, 3)  # deliveries_per_month, avg_rating, refund_count

# Scale the features
scaler = StandardScaler()
X_eta_scaled = scaler.fit_transform(X_eta)
X_fraud_scaled = scaler.transform(X_fraud)
X_cluster_scaled = scaler.transform(X_cluster)

# Train ETA Predictor
eta_model = LinearRegression()
eta_model.fit(X_eta_scaled, y_eta)

# Train Fraud Detector
fraud_model = RandomForestClassifier(n_estimators=100, random_state=42)
fraud_model.fit(X_fraud_scaled, y_fraud)

# Train Customer Segmenter
cluster_model = KMeans(n_clusters=3, random_state=42)
cluster_model.fit(X_cluster_scaled)

# Save models
joblib.dump(eta_model, f'{model_dir}/eta_predictor.joblib')
joblib.dump(fraud_model, f'{model_dir}/fraud_detector.joblib')
joblib.dump(cluster_model, f'{model_dir}/customer_segmenter.joblib')
joblib.dump(scaler, f'{model_dir}/scaler.joblib')

print("Models trained and saved successfully!") 