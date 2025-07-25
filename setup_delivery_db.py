import mysql.connector
from mysql.connector import Error
#dont use this , created manually, lol 
def create_database():
    try:
        connection = mysql.connector.connect(
            host="localhost",
            user="root",  
            password="lavi9755"  
        )
        
        if connection.is_connected():
            cursor = connection.cursor()
            
            # Create database
            cursor.execute("CREATE DATABASE IF NOT EXISTS delivery_db")
            print("Database 'delivery_db' created successfully")
            
            cursor.execute("USE delivery_db")
            
        
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    password_hash VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """)
            
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    address TEXT NOT NULL,
                    phone VARCHAR(20),
                    email VARCHAR(100),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """)
            
            
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                    order_id INT AUTO_INCREMENT PRIMARY KEY,
                    customer_id INT,
                    order_status VARCHAR(20) DEFAULT 'pending',
                    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    delivery_address TEXT NOT NULL,
                    total_amount DECIMAL(10, 2) NOT NULL,
                    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
                )
            """)
            
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS deliveries (
                    delivery_id INT AUTO_INCREMENT PRIMARY KEY,
                    order_id INT,
                    driver_id INT,
                    status VARCHAR(20) DEFAULT 'assigned',
                    pickup_time DATETIME,
                    delivery_time DATETIME,
                    FOREIGN KEY (order_id) REFERENCES orders(order_id),
                    FOREIGN KEY (driver_id) REFERENCES users(user_id)
                )
            """)
            
            print("All tables created successfully!")
            
    except Error as e:
        print(f"Error: {e}")
    
    finally:
        if 'connection' in locals() and connection.is_connected():
            cursor.close()
            connection.close()
            print("MySQL connection closed.")

if __name__ == "__main__":
    create_database() 