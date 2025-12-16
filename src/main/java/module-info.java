module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    // 🔥 REQUIRED FOR *ALL* FXML CONTROLLERS
    opens model.controller to javafx.fxml;

    // Optional (for TableView / PropertyValueFactory, etc.)
    opens model.restaurant to javafx.base;

    exports com.example.demo2;
}


