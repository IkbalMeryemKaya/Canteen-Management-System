/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databasesystem_project;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author imans
 */
public class DBConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=CanteenInventoryDB;encrypt=false;";
    private static final String USER = "YOUR_USERNAME";
    private static final String PASSWORD = "YOUR_PASSWORD";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Bağlantı hatası: " + e.getMessage());
            return null;
        }
    }
}
