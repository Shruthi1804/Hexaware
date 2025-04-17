package com.hexaware.dao;

import com.hexaware.entity.Product;
import com.hexaware.entity.User;
import com.hexaware.util.DbConnectionUtil;
import com.hexaware.util.enums.ProductType;
import com.hexaware.util.enums.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderManagement implements IOrderManagementRepository {

    @Override
    public void createUser(User user) throws Exception {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DbConnectionUtil.getDbConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().toString());
            ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void createProduct(User user, Product product) throws Exception {
        if (user.getRole() != UserRole.ADMIN) {
            throw new Exception("Only admin can create products.");
        }

        String sql = "INSERT INTO products (product_name, description, price, quantity_in_stock, type) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DbConnectionUtil.getDbConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantityInStock());
            ps.setString(5, product.getType().name());
            ps.executeUpdate();
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void createOrder(User user, List<Product> products) throws Exception {
        String orderSql = "INSERT INTO orders (user_id) VALUES (?)";
        String orderProductSql = "INSERT INTO order_products (order_id, product_id, quantity) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement orderProductStmt = null;
        ResultSet rs = null;

        try {
            conn = DbConnectionUtil.getDbConnection();
            orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderProductStmt = conn.prepareStatement(orderProductSql);

            orderStmt.setInt(1, user.getUserId());
            orderStmt.executeUpdate();

            rs = orderStmt.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            for (Product product : products) {
                orderProductStmt.setInt(1, orderId);
                orderProductStmt.setInt(2, product.getProductId());
                orderProductStmt.setInt(3, 1);
                orderProductStmt.addBatch();
            }
            orderProductStmt.executeBatch();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (orderProductStmt != null) {
                orderProductStmt.close();
            }
            if (orderStmt != null) {
                orderStmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void cancelOrder(int userId, int orderId) throws Exception {
        String checkUserSql = "SELECT user_id FROM orders WHERE order_id = ?";
        String deleteOrderProducts = "DELETE FROM order_products WHERE order_id = ?";
        String deleteOrder = "DELETE FROM orders WHERE order_id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        try {
            conn = DbConnectionUtil.getDbConnection();
            ps = conn.prepareStatement(checkUserSql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int dbUserId = rs.getInt("user_id");
                if (dbUserId != userId) {
                    throw new Exception("Unauthorized to cancel this order.");
                }
            } else {
                throw new Exception("Order not found.");
            }

            ps1 = conn.prepareStatement(deleteOrderProducts);
            ps2 = conn.prepareStatement(deleteOrder);
            ps1.setInt(1, orderId);
            ps2.setInt(1, orderId);
            ps1.executeUpdate();
            ps2.executeUpdate();
        } finally {
            if (ps2 != null) {
                ps2.close();
            }
            if (ps1 != null) {
                ps1.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbConnectionUtil.getDbConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantityInStock(rs.getInt("quantity_in_stock"));
                product.setType(ProductType.valueOf(rs.getString("type")));
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return productList;
    }

    @Override
    public List<Product> getOrderByUser(User user) throws Exception {
        List<Product> orderedProducts = new ArrayList<>();
        String sql = "SELECT p.* FROM products p " +
                "JOIN order_products op ON p.product_id = op.product_id " +
                "JOIN orders o ON o.order_id = op.order_id " +
                "WHERE o.user_id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbConnectionUtil.getDbConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantityInStock(rs.getInt("quantity_in_stock"));
                product.setType(ProductType.valueOf(rs.getString("type")));
                orderedProducts.add(product);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return orderedProducts;
    }
}
