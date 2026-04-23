package com.mycompany.vehicleservicesystem;

public class Main {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("  Vehicle Service Management System  ");
        System.out.println("======================================\n");

        CustomerDAO       customerDAO    = new CustomerDAO();
        VehicleDAO        vehicleDAO     = new VehicleDAO();
        ServiceRecordDAO  serviceDAO     = new ServiceRecordDAO();

        // STEP 1 — Add a new customer
        System.out.println(">>> STEP 1: Adding Customer...");
        customerDAO.addCustomer("Amit Patel", "9823456789",
                                "amit@gmail.com", "Nashik, MH");

        // STEP 2 — View all customers
        System.out.println("\n>>> STEP 2: Viewing All Customers...");
        customerDAO.getAllCustomers();

        // STEP 3 — Add a vehicle (customer_id=2 is Amit, seeded id=1 is Rahul)
        System.out.println("\n>>> STEP 3: Adding Vehicle...");
        vehicleDAO.addVehicle(2, "MH04XY9876", "Maruti", "Swift", "Hatchback");

        // STEP 4 — View all vehicles
        System.out.println("\n>>> STEP 4: Viewing All Vehicles...");
        vehicleDAO.getAllVehicles();

        // STEP 5 — Create a service record for vehicle_id = 2
        System.out.println("\n>>> STEP 5: Creating Service Record...");
        int recordId = serviceDAO.createServiceRecord(
                           2, "2025-04-20", "2025-04-21", "In Progress");

        // STEP 6 — Assign services (1=Oil Change Rs.500, 3=Battery Check Rs.200)
        System.out.println("\n>>> STEP 6: Assigning Services...");
        serviceDAO.addServiceToRecord(recordId, 1);
        serviceDAO.addServiceToRecord(recordId, 3);

        // STEP 7 — View all service records
        System.out.println("\n>>> STEP 7: Viewing Service Records...");
        serviceDAO.getAllServiceRecords();

        // STEP 8 — Generate bill (auto-calculates total from assigned services)
        System.out.println("\n>>> STEP 8: Generating Bill...");
        serviceDAO.generateBill(recordId, "Cash");

        // STEP 9 — Print the final bill
        System.out.println("\n>>> STEP 9: Viewing Bill...");
        serviceDAO.viewBill(recordId);

        System.out.println("\nAll operations completed successfully!");
    }
}