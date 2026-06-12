# Canteen Management System

A desktop inventory and sales management system for a university canteen, built with Java Swing and Microsoft SQL Server. The system supports both admin and employee roles, covering product/employee management, order processing, sales tracking, stock monitoring, and supplier purchase orders.

> Group project — developed as part of the *Database System Design* course, by a 3-person team (İkbal Meryem Kaya, İman Saeid, Sedra Alshaar).

## Features

- **Role-based access** — separate Admin and Employee login screens with different permissions.
- **Product & employee management** — admins can add/remove products (with images, categories, pricing) and employees.
- **Order processing** — employees create orders via a categorized product menu (Food, Cold/Hot Drinks, Fast Food, Sweets, Snacks), with a live order table and total calculation.
- **Sales tracking** — view completed sales transactions with item-level detail (quantity, unit price, total).
- **Stock management** — real-time stock levels per product, with automatic low-stock / out-of-stock alerts based on configurable reorder thresholds.
- **Purchase orders** — create supplier purchase orders to restock products, tracking supplier, quantity, unit price, and total cost.
- **Discounts & payment types** — database schema supports discount codes and multiple payment methods.

## Tech Stack

- **Language:** Java (Swing, NetBeans GUI Builder)
- **Database:** Microsoft SQL Server (JDBC)
- **Architecture:** Multi-frame desktop application with a shared `DBConnection` class for database access

## Database Design

The system is backed by `CanteenInventoryDB`, with relational tables covering:
- **Catalog:** `Product`, `Category`, `Unit`, `FoodProduct`, `Supplier`
- **People:** `Employee` (role-based: Admin / Employee)
- **Sales:** `Sales`, `SalesItems`, `ReturnSale`, `PaymentType`, `Discount`
- **Purchasing:** `PurchaseOrder`, `PurchaseOrderItems`
- **Inventory:** `Stock`, `StockTransactions`, `StockAlert`, `StockReorder`

Foreign key constraints maintain referential integrity across sales, purchases, and stock records. The full schema (with ER diagram and SQL scripts) is included in the repo.

## Application Flow

1. **Login** — Admin or Employee logs in with credentials validated against the `Employee` table.
2. **Admin panel** — manage products and employees (add/remove).
3. **Employee menu** — access Make Order, Sales Item, Stock, Reorder, and Purchase screens.
4. **Make Order** — browse products by category, add items to the order, and confirm.
5. **Stock** — view current inventory; system automatically flags products that need reordering.
6. **Purchase Order** — select products/suppliers to restock and finalize the order.

## Setup

1. Create the `CanteenInventoryDB` database in SQL Server using the provided SQL scripts (schema + initial data).
2. Update the connection settings in `DBConnection.java` with your own SQL Server credentials.
3. Open the project in NetBeans and run `Admin_Login.java` or `Employee_Login.java` as the main class.

## What I Learned

This project gave me hands-on experience designing a relational database from scratch — modeling entities, relationships, and constraints for a real-world inventory system — and connecting a Java Swing front-end to SQL Server via JDBC. I worked on the GUI logic and database integration for the admin management (add/remove product & employee) and inventory/order modules.
