-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS delivery_db;
USE delivery_db;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS feedbacks;
DROP TABLE IF EXISTS deliveries;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    picture VARCHAR(255),
    google_id VARCHAR(255) UNIQUE,
    role ENUM('ROLE_USER', 'ROLE_ADMIN') DEFAULT 'ROLE_USER' NOT NULL
);

-- Create customers table
CREATE TABLE customers (
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Create orders table
CREATE TABLE orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_status VARCHAR(20) DEFAULT 'pending' NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    delivery_address TEXT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Create deliveries table
CREATE TABLE deliveries (
    delivery_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_id BIGINT NULL,
    driver_id BIGINT NOT NULL,
    source VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    weight DOUBLE NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    booking_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    pickup_time DATETIME,
    delivery_time DATETIME,
    estimated_delivery_time DATETIME DEFAULT (NOW() + INTERVAL 30 MINUTE),
    actual_delivery_time DATETIME,
    delivery_status ENUM('PENDING', 'PICKED_UP', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING' NOT NULL,
    is_fraudulent BOOLEAN DEFAULT FALSE NOT NULL,
    predicted_eta DOUBLE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (driver_id) REFERENCES users(user_id)
);

-- Create feedback table
CREATE TABLE feedbacks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    delivery_id BIGINT NOT NULL,
    rating INTEGER NOT NULL,
    comment TEXT,
    sentiment_score DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (delivery_id) REFERENCES deliveries(delivery_id)
);

-- Add indexes for better performance
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_google_id ON users(google_id);
CREATE INDEX idx_customer_email ON customers(email);
CREATE INDEX idx_order_customer ON orders(customer_id);
CREATE INDEX idx_delivery_user ON deliveries(user_id);
CREATE INDEX idx_delivery_status ON deliveries(delivery_status);
CREATE INDEX idx_feedback_user ON feedbacks(user_id); 