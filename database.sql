CREATE DATABASE finance_management;

USE finance_management;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50)
);

CREATE TABLE transactions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    amount DOUBLE,
    category VARCHAR(50),
    date DATE
);