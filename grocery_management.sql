create database grocery_management;
use grocery_management;

CREATE TABLE Customers (
  customer_id INT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  address VARCHAR(255) NOT NULL);

CREATE TABLE Credit (
  customer_id INT PRIMARY KEY,
  credit DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE CASCADE);

CREATE TABLE Vendors (
  vendor_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  email VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL);

CREATE TABLE Products (
  product_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  vendor_id INT,
  price DECIMAL(10,2) NOT NULL,quantity INT,
  FOREIGN KEY (vendor_id) REFERENCES Vendors(vendor_id) ON DELETE SET NULL);

CREATE TABLE Transactions (
  transaction_id INT PRIMARY KEY AUTO_INCREMENT,
  customer_id INT,
  product_id INT,
  Transaction_type VARCHAR(255) NOT NULL,
  Amount INT ,
  FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE);

CREATE TABLE Coupons (
  coupon_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(255) NOT NULL,
  discount DECIMAL(10,2) NOT NULL,
  expiration_date DATE NOT NULL);

CREATE TABLE Rewards (
  reward_id INT PRIMARY KEY AUTO_INCREMENT,
  customer_id INT,
  points INT NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE CASCADE);

