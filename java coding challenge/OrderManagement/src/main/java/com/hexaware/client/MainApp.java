package com.hexaware.client;

import com.hexaware.dao.OrderManagement;
import com.hexaware.entity.Product;
import com.hexaware.entity.User;
import com.hexaware.util.enums.ProductType;
import com.hexaware.util.enums.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    private static OrderManagement orderService = new OrderManagement();
    private static User loggedInUser = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Order Management Console App =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Create Product (Admin only)");
            System.out.println("4. View All Products");
            System.out.println("5. Place Order");
            System.out.println("6. View My Orders");
            System.out.println("7. Cancel Order");
            System.out.println("0. Exit");

            System.out.print("Select option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (option) {
                    case 1 -> registerUser();
                    case 2 -> loginUser();
                    case 3 -> createProduct();
                    case 4 -> viewAllProducts();
                    case 5 -> placeOrder();
                    case 6 -> viewMyOrders();
                    case 7 -> cancelOrder();
                    case 0 -> {
                        System.out.println("Exiting...");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void registerUser() throws Exception {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter role (0 for ADMIN / 1 for CUSTOMER): ");
        int roleInput = Integer.parseInt(scanner.nextLine());

        UserRole role;
        if (roleInput == 0) {
            role = UserRole.ADMIN;
        } else if (roleInput == 1) {
            role = UserRole.USER;
        } else {
            throw new IllegalArgumentException("Invalid role input. Please enter 0 for ADMIN or 1 for CUSTOMER.");
        }

        User user = new User(username, password, role);
        orderService.createUser(user);
        System.out.println("User registered successfully.");
    }

    private static void loginUser() {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter role (0 for ADMIN / 1 for CUSTOMER): ");
        int roleInput = Integer.parseInt(scanner.nextLine());

        UserRole role;
        if (roleInput == 0) {
            role = UserRole.ADMIN;
        } else if (roleInput == 1) {
            role = UserRole.USER;
        } else {
            System.out.println("Invalid role input. Please enter 0 or 1.");
            return;
        }

        loggedInUser = new User();
        loggedInUser.setUserId(userId);
        loggedInUser.setUsername(username);
        loggedInUser.setRole(role);

        System.out.println("Login successful.");
    }

    private static void createProduct() throws Exception {
        if (loggedInUser == null || loggedInUser.getRole() != UserRole.ADMIN) {
            System.out.println("Only ADMIN can create products.");
            return;
        }

        System.out.print("Product name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        System.out.print("Quantity in stock: ");
        int qty = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Type (ELECTRONICS, GROCERY, etc.): ");
        String type = scanner.nextLine().toUpperCase();

        Product product = new Product(name, desc, price, qty, ProductType.valueOf(type));
        orderService.createProduct(loggedInUser, product);
        System.out.println("Product created successfully.");
    }

    private static void viewAllProducts() {
        List<Product> products = orderService.getAllProducts();
        System.out.println("\n--- Product List ---");
        for (Product p : products) {
            System.out.println(p.getProductId() + " - " + p.getProductName() + " | " + p.getPrice());
        }
    }

    private static void placeOrder() throws Exception {
        if (loggedInUser == null) {
            System.out.println("Please login first.");
            return;
        }

        viewAllProducts();
        System.out.print("Enter comma-separated product IDs to order (e.g., 1,2,3): ");
        String[] input = scanner.nextLine().split(",");
        List<Product> productList = new ArrayList<>();

        for (String idStr : input) {
            int id = Integer.parseInt(idStr.trim());
            Product p = new Product();
            p.setProductId(id);
            productList.add(p);
        }

        orderService.createOrder(loggedInUser, productList);
        System.out.println("Order placed successfully.");
    }

    private static void viewMyOrders() throws Exception {
        if (loggedInUser == null) {
            System.out.println("Please login first.");
            return;
        }

        List<Product> orders = orderService.getOrderByUser(loggedInUser);
        System.out.println("\n--- My Orders ---");
        for (Product p : orders) {
            System.out.println(p.getProductName() + " | Price: " + p.getPrice());
        }
    }

    private static void cancelOrder() throws Exception {
        if (loggedInUser == null) {
            System.out.println("Please login first.");
            return;
        }

        System.out.print("Enter Order ID to cancel: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();  

        orderService.cancelOrder(loggedInUser.getUserId(), orderId);
        System.out.println("Order canceled successfully.");
    }
}
