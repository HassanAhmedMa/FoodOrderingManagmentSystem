package model.controller;

import dao.MenuItemDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.restaurant.MenuItem;

public class EditMenuItemController {

    @FXML private TextField nameField;
    @FXML private TextArea descField;
    @FXML private TextField priceField;
    @FXML private TextField categoryField;
    @FXML private CheckBox availableCheck;

    private MenuItem item;
    private RestaurantDashboardController parent;

    private final MenuItemDAO menuItemDAO = new MenuItemDAO();

    // 🔑 called BEFORE showing popup
    public void setData(MenuItem item, RestaurantDashboardController parent) {
        this.item = item;
        this.parent = parent;

        nameField.setText(item.getName());
        descField.setText(item.getDescription());
        priceField.setText(String.valueOf(item.getPrice()));
        categoryField.setText(item.getCategory());
        availableCheck.setSelected(item.isAvailable());
    }

    @FXML
    private void onSave() {
        try {
            item.setDescription(descField.getText());
            item.setCategory(categoryField.getText());
            item.setAvailable(availableCheck.isSelected());

            item.setRestaurantId(item.getRestaurantId()); // unchanged
            item.setItemId(item.getItemId());

            // setters you must have
            item.setName(nameField.getText());
            item.setPrice(Double.parseDouble(priceField.getText()));

            menuItemDAO.updateMenuItem(item);

            if (parent != null) {
                parent.refreshMenu();
            }

            close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
