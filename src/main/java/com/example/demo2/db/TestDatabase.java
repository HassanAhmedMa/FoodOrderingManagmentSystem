package com.example.demo2.db;

import com.example.demo2.db.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDatabase {


    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("✅ Connected to database!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("user_id") + " - " +
                                rs.getString("full_name") + " (" +
                                rs.getString("role") + ")"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
