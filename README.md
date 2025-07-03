# Smart Delivery Assistant – AI Enhanced Edition

A modern delivery management system enhanced with AI capabilities for accurate ETA prediction, fraud detection, customer segmentation, and sentiment analysis.

## Features

- 🚚 Real-time delivery tracking
- 🤖 AI-powered ETA prediction
- 🔍 Fraud detection system
- 👥 Customer segmentation
- 📊 Analytics dashboard
- 🔐 Google OAuth2 authentication

## Tech Stack

- **Backend**: Spring Boot 3.2+
- **Frontend**: React
- **Database**: MySQL
- **AI/ML**: Python (scikit-learn, TextBlob)
- **API**: Flask
- **Authentication**: OAuth2 (Google)

## Prerequisites

- Java 17+
- Python 3.8+
- MySQL 8.0+
- Node.js 18+
- Maven 3.8+

## Setup Instructions

### 1. Database Setup

```sql
CREATE DATABASE delivery_db;
```

### 2. Backend Setup

1. Configure application.properties:
   - Update MySQL credentials
   - Add Google OAuth2 credentials

2. Build and run the Spring Boot application:
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 3. ML API Setup

1. Set up Python virtual environment:
```bash
cd flask-ml-api
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
```

2. Train the ML models:
```bash
python train_models.py
```

3. Start the Flask server:
```bash
python app.py
```

### 4. Frontend Setup

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Start the development server:
```bash
npm start
```

## API Endpoints

### Spring Boot Backend

- `POST /api/delivery/book` - Book a new delivery
- `GET /api/delivery/list` - List user's deliveries
- `GET /api/admin/dashboard` - Get analytics data

### Flask ML API

- `POST /predict_eta` - Predict delivery ETA
- `POST /detect_fraud` - Detect fraudulent activity
- `POST /cluster_user` - Segment customer

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
