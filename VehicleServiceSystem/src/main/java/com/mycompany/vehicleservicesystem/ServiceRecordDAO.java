package com.mycompany.vehicleservicesystem;

import java.sql.*;

public class ServiceRecordDAO {

    public int createServiceRecord(int vehicleId, String inDate,
                                   String serviceDate, String status) {
        String sql = "INSERT INTO Service_Record "
                   + "(vehicle_id, in_date, service_date, status) "
                   + "VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, vehicleId);
            ps.setString(2, inDate);
            ps.setString(3, serviceDate);
            ps.setString(4, status);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                generatedId = keys.getInt(1);
                System.out.println("Service Record created. ID: " + generatedId);
            }

        } catch (SQLException e) {
            System.out.println("Error creating service record: " + e.getMessage());
        }
        return generatedId;
    }

    public void addServiceToRecord(int recordId, int serviceId) {
        String sql = "INSERT INTO Service_Details (record_id, service_id) "
                   + "VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recordId);
            ps.setInt(2, serviceId);
            ps.executeUpdate();
            System.out.println("Service ID " + serviceId
                             + " linked to Record " + recordId);

        } catch (SQLException e) {
            System.out.println("Error linking service: " + e.getMessage());
        }
    }

    public void generateBill(int recordId, String paymentMode) {
        String calcSQL = "SELECT SUM(s.cost) AS total "
                       + "FROM Service_Details sd "
                       + "JOIN Service s ON sd.service_id = s.service_id "
                       + "WHERE sd.record_id = ?";
        String billSQL = "INSERT INTO Bill "
                       + "(record_id, total_amount, payment_mode, payment_status) "
                       + "VALUES (?, ?, ?, 'Pending')";
        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps1 = conn.prepareStatement(calcSQL);
            ps1.setInt(1, recordId);
            ResultSet rs = ps1.executeQuery();
            double total = 0;
            if (rs.next()) {
                total = rs.getDouble("total");
            }

            PreparedStatement ps2 = conn.prepareStatement(billSQL);
            ps2.setInt(1, recordId);
            ps2.setDouble(2, total);
            ps2.setString(3, paymentMode);
            ps2.executeUpdate();

            System.out.println("Bill generated | Record: " + recordId
                             + " | Total: Rs." + total
                             + " | Mode: " + paymentMode);

        } catch (SQLException e) {
            System.out.println("Error generating bill: " + e.getMessage());
        }
    }

    public void getAllServiceRecords() {
        String sql = "SELECT sr.record_id, v.vehicle_number, c.name, "
                   + "sr.in_date, sr.service_date, sr.status "
                   + "FROM Service_Record sr "
                   + "JOIN Vehicle v ON sr.vehicle_id = v.vehicle_id "
                   + "JOIN Customer c ON v.customer_id = c.customer_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- All Service Records ---");
            System.out.printf("%-5s %-15s %-20s %-12s %-12s %-12s%n",
                              "ID", "Vehicle No", "Customer",
                              "In Date", "Svc Date", "Status");
            System.out.println("-".repeat(80));
            while (rs.next()) {
                System.out.printf("%-5d %-15s %-20s %-12s %-12s %-12s%n",
                    rs.getInt("record_id"),
                    rs.getString("vehicle_number"),
                    rs.getString("name"),
                    rs.getString("in_date"),
                    rs.getString("service_date"),
                    rs.getString("status"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching records: " + e.getMessage());
        }
    }

    public void viewBill(int recordId) {
        String sql = "SELECT b.bill_id, b.total_amount, b.payment_mode, "
                   + "b.payment_status, "
                   + "GROUP_CONCAT(s.service_name SEPARATOR ', ') AS services "
                   + "FROM Bill b "
                   + "JOIN Service_Details sd ON b.record_id = sd.record_id "
                   + "JOIN Service s ON sd.service_id = s.service_id "
                   + "WHERE b.record_id = ? "
                   + "GROUP BY b.bill_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recordId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n========== BILL ==========");
            if (rs.next()) {
                System.out.println("Bill ID       : " + rs.getInt("bill_id"));
                System.out.println("Services Done : " + rs.getString("services"));
                System.out.println("Total Amount  : Rs." + rs.getDouble("total_amount"));
                System.out.println("Payment Mode  : " + rs.getString("payment_mode"));
                System.out.println("Status        : " + rs.getString("payment_status"));
            }
            System.out.println("==========================");

        } catch (SQLException e) {
            System.out.println("Error viewing bill: " + e.getMessage());
        }
    }
}