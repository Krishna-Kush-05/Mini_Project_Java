# Mini_Project_Java
DBMS + Java Mini Project Sem 4
# 🚗 Vehicle Service Management System

A desktop application for managing vehicle service records, customers, vehicles, and billing. Built as a mini-project for DBMS using Java Swing and MySQL.

![Java](https://img.shields.io/badge/Java-17+-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![Maven](https://img.shields.io/badge/Maven-3.8+-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📌 Features

- **Customer Management** – Add and view customer details.
- **Vehicle Management** – Register vehicles with ownership linking.
- **Service Records** – Create service entries, assign services, auto‑fill dates.
- **Billing** – Generate bills with automatic total calculation, mark as paid.
- **Clean UI** – Tabbed interface with tables and intuitive forms.
- **Database Integrity** – Foreign key constraints enforced.

---

## 🛠️ Tech Stack

| Layer       | Technology |
|-------------|------------|
| Frontend    | Java Swing |
| Backend     | JDBC (DAO Pattern) |
| Database    | MySQL |
| Build Tool  | Apache Maven |
| IDE         | Apache NetBeans |

---

## 📊 Database Schema


The system uses 6 tables:
- `Customer`
- `Vehicle`
- `Service_Record`
- `Service`
- `Service_Details` (junction table)
- `Bill`

Foreign key relationships ensure data consistency.

---

## 🚀 How to Run Locally

### Prerequisites
- Java JDK 8 or higher
- MySQL Server (8.0+)
- Apache Maven
- NetBeans (or any Java IDE)
