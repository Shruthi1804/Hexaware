package com.hexaware.util;

public class Constants {

    public static final String DB_FILE_NAME = "src/main/java/db.properties";
    public static final String DB_DRIVER = "driver";
    public static final String DB_URL= "dburl";
    public static final String CANNOT_OPEN_CONNECTION = "Connection cannot be opened";



    public static final String INSERT_USER_QUERY = "INSERT INTO users (user_name, password) VALUES (?, ?)";
    public static final String INSERT_PRODUCT_QUERY = "INSERT INTO products (product_name, price, created_by) VALUES (?, ?, ?)";
    public static final String INSERT_ORDER_QUERY = "INSERT INTO orders (user_id, order_date) VALUES (?, ?)";
    public static final String INSERT_ORDER_PRODUCT_QUERY = "INSERT INTO order_products (order_id, product_id, quantity) VALUES (?, ?, ?)";
    public static final String CANCEL_ORDER_QUERY = "DELETE FROM orders WHERE user_id = ? AND order_id = ?";
    public static final String GET_ALL_PRODUCTS_QUERY = "SELECT * FROM products";
    public static final String GET_PRODUCTS_BY_USER_QUERY = "SELECT p.product_id, p.product_name, p.price FROM products p JOIN order_products op ON p.product_id = op.product_id JOIN orders o ON o.order_id = op.order_id WHERE o.user_id = ?";

}
