
# 🛒 Grocery Management System

A simple **Grocery Management System** implemented in **Python** and **Java** using **MySQL** as the backend database.  
This project demonstrates how to perform **CRUD operations** (Create, Read, Update, Delete) on grocery items using database connectivity.

---

## 📋 Features

- ➕ Add new grocery items  
- ✏️ Update existing item details  
- ❌ Delete grocery items  
- 📄 View all grocery items  
- 🗄️ MySQL database integration  
- 💾 Persistent storage for grocery data  
- ⚙️ Error handling for invalid operations  

---

## 🧠 Technologies Used

| Component | Technology |
|------------|-------------|
| **Programming Languages** | Python, Java |
| **Database** | MySQL |
| **Python Library** | `mysql.connector` |
| **Java Library** | JDBC (Java Database Connectivity) |
| **IDE / Tools** | VS Code / IntelliJ IDEA / MySQL Workbench |

---

## 📦 Project Structure

```

📁 Grocery-Management-System
├── grocery_management.py         # Python implementation
├── GroceryManagementSystem.java  # Java implementation
├── grocery_db.sql                # Database schema file
├── screenshots/                  # Folder containing UI/output screenshots
│   ├── python_output.png
│   ├── java_output.png
│   └── er_diagram.png
├── README.md                     # Project documentation
└── LICENSE                       # Open-source license (MIT)

````

---

## 🗄️ Database Schema (MySQL)

You can create the database manually or execute the `grocery_db.sql` file.

### SQL Script
```sql
CREATE DATABASE grocery_db;
USE grocery_db;

CREATE TABLE groceries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price FLOAT NOT NULL
);
````

---

## 🧩 Entity–Relationship (ER) Diagram

### 🧱 Text Representation

```
+-------------------+
|     GROCERY       |
+-------------------+
| id (PK)           |
| name              |
| quantity          |
| price             |
+-------------------+
```

📝 **Explanation:**

* **GROCERY** is the main entity representing each item.
* Each record has:

  * `id` → Primary Key (unique identifier)
  * `name` → Grocery item name
  * `quantity` → Available stock quantity
  * `price` → Price per unit/item

### 🖼️ Visual Representation of Database

<img width="999" height="771" alt="file_2025-10-31_18 29 25" src="https://github.com/user-attachments/assets/8f7e6e2e-5df1-4ee1-a8b8-53911b760a94" />

---
# Setup and Installation

## 🐍 Python Version

The Python version uses the `mysql.connector` library for MySQL connectivity.

### ▶️ Steps to Run

1. **Install dependencies**

   ```bash
   pip install mysql-connector-python
   ```

2. **Configure database credentials** inside `grocery_management.py`:

   ```python
   connection = mysql.connector.connect(
       host="localhost",
       user="root",
       password="your_password",
       database="grocery_db"
   )
   ```

3. **Run the program**

   ```bash
   python grocery_management.py
   ```

---

## ☕ Java Version

The Java version uses **JDBC** for database communication.

### ▶️ Steps to Run

1. **Download and add MySQL Connector/J**

   * Download: [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
   * Add the `.jar` file to your project’s classpath.

2. **Configure database credentials** inside `GroceryManagementSystem.java`:

   ```java
   Connection connection = DriverManager.getConnection(
       "jdbc:mysql://localhost:3306/grocery_db", "root", "your_password"
   );
   ```

3. **Compile and run**

   ```bash
   javac GroceryManagementSystem.java
   java GroceryManagementSystem
   ```

---

## 🖼️ Screenshots
<img width="449.76" height="329.28" alt="image" src="https://github.com/user-attachments/assets/9ce35154-05b5-4daf-a721-9f81da3cd11a" />
<img width="449.76" height="329.28" alt="image" src="https://github.com/user-attachments/assets/edb24bdb-7d1b-49b5-83ca-514f02da4717" />


---

## 💡 Concepts Demonstrated

* CRUD operations (Create, Read, Update, Delete)
* Database connectivity with MySQL
* Parameterized queries and SQL security
* Exception handling
* Menu-driven console applications
* Integration of Python and Java with the same database

---

## ⚙️ Default Configuration

| Setting      | Value         |
| ------------ | ------------- |
| **Host**     | localhost     |
| **Port**     | 3306          |
| **User**     | root          |
| **Password** | your_password |
| **Database** | grocery_db    |

---

## 🧑‍💻 Author

- **Jay Chandak**
🎓 MU | TE (AI & ML)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/pushkar2510) 
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:pushkargaikwad25@gmail.com)
- **Pushkar Gaikwad**
🎓 SPPU | TE (AI & DS)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/pushkar2510) 
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:pushkargaikwad25@gmail.com)
- **Aditya Yadav**
🎓 SPPU | TE (AI & DS)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/pushkar2510) 
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:pushkargaikwad25@gmail.com)
- **Yash Tongale**
🎓 SPPU | TE (AI & DS)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/pushkar2510) 
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:pushkargaikwad25@gmail.com)
- **Yash Zoman**
🎓 SPPU | TE (AI & DS)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/pushkar2510) 
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:pushkargaikwad25@gmail.com)

---

## 🪪 License

This project is open-source and available under the **[MIT License](LICENSE)**.

---
