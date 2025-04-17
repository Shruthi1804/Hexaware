package com.hexaware.dao;

import com.hexaware.entity.Product;
import com.hexaware.entity.User;

import java.util.List;

public interface IOrderManagementRepository {
    void createOrder(User user, List<Product> products) throws Exception;
    void cancelOrder(int userId, int orderId) throws Exception;
    void createProduct(User user, Product product) throws Exception;
    void createUser(User user) throws Exception;
    List<Product> getAllProducts();
    List<Product> getOrderByUser(User user) throws Exception;
}



