package com.example.demo2;

import com.example.demo2.db.DBConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("✅ Connected to database!");

            Statement stmt =  conn.createStatement();
            ResultSet rs =  stmt.executeQuery("SELECT * FROM users");

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

        launch();

    }
}