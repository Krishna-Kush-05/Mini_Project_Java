package com.mycompany.vehicleservicesystem;

import java.sql.*;

public class VehicleDAO {

    public void addVehicle(int customerId, String vehicleNumber,
                           String brand, String model, String type) {
        String sql = "INSERT INTO Vehicle (customer_id, vehicle_number, brand, model, type) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ps.setString(2, vehicleNumber);
            ps.setString(3, brand);
            ps.setString(4, model);
            ps.setString(5, type);
            ps.executeUpdate();
            System.out.println("Vehicle added: " + vehicleNumber);

        } catch (SQLException e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    public void getAllVehicles() {
        String sql = "SELECT v.vehicle_id, c.name, v.vehicle_number, "
                   + "v.brand, v.model, v.type "
                   + "FROM Vehicle v "
                   + "JOIN Customer c ON v.customer_id = c.customer_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- All Vehicles ---");
            System.out.printf("%-5s %-20s %-15s %-12s %-12s %-10s%n",
                              "ID", "Owner", "Veh.No", "Brand", "Model", "Type");
            System.out.println("-".repeat(78));
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-15s %-12s %-12s %-10s%n",
                    rs.getInt("vehicle_id"),
                    rs.getString("name"),
                    rs.getString("vehicle_number"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getString("type"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching vehicles: " + e.getMessage());
        }
    }
}