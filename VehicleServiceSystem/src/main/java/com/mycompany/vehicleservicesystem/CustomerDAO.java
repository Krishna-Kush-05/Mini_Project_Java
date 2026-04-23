package com.mycompany.vehicleservicesystem;

import java.sql.*;

public class CustomerDAO {

    public void addCustomer(String name, String phone,
                            String email, String address) {

        String checkSql = "SELECT * FROM Customer WHERE phone = ?";
        String insertSql = "INSERT INTO Customer (name, phone, email, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement check = conn.prepareStatement(checkSql)) {

            // 🔍 Check if customer already exists
            check.setString(1, phone);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                System.out.println("Customer already exists!");
                return;
            }

            // ✅ Insert new customer
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, name);
                ps.setString(2, phone);
                ps.setString(3, email);
                ps.setString(4, address);
                ps.executeUpdate();
                System.out.println("Customer added: " + name);
            }

        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    public void getAllCustomers() {
        String sql = "SELECT * FROM Customer";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- All Customers ---");
            System.out.printf("%-5s %-20s %-15s %-25s %-20s%n",
                              "ID", "Name", "Phone", "Email", "Address");
            System.out.println("-".repeat(88));

            while (rs.next()) {
                System.out.printf("%-5d %-20s %-15s %-25s %-20s%n",
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching customers: " + e.getMessage());
        }
    }
}