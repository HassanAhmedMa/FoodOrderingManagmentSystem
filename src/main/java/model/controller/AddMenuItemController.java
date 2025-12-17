package model.controller;

import dao.MenuItemDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.restaurant.MenuItem;

public class AddMenuItemController {

    // ================= FXML =================
    @FXML private TextField nameField;
    @FXML private TextArea descField;
    @FXML private TextField priceField;
    @FXML private TextField categoryField;
    @FXML private CheckBox availableCheck;

    // ================= DAO =================
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();

    // ================= PARENT =================
    private RestaurantDashboardController parent;

    public void setParent(RestaurantDashboardController parent) {
        this.parent = parent;
    }

    // ================= SAVE =================
    @FXML
    private void onSave() {

        try {
            String name = nameField.getText();
            String desc = descField.getText();
            String category = categoryField.getText();
            boolean available = availableCheck.isSelected();
            double price = Double.parseDouble(priceField.getText());

            if (name.isBlank() || category.isBlank()) {
                showError("Name and category are required.");
                return;
            }

            MenuItem item = new MenuItem(
                    parent.getRestaurant().getId(),
                    name,
                    desc,
                    price,
                    category,
                    available
            );

            menuItemDAO.addMenuItem(item);

            // refresh dashboard
            parent.refreshMenu();

            closeWindow();

        } catch (NumberFormatException e) {
            showError("Price must be a valid number.");
        }
    }

    // ================= CANCEL =================
    @FXML
    private void onCancel() {
        closeWindow();
    }

    // ================= HELPERS =================
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
