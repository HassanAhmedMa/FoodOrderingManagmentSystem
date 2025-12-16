package model.controller;

import dao.MenuItemDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.restaurant.MenuItem;

public class RestaurantMenuItemCardController {

    @FXML private Label nameLabel;
    @FXML private Label descLabel;
    @FXML private Label priceLabel;
    @FXML private Label statusLabel;
    @FXML private ImageView itemImage;

    private MenuItem item;
    private RestaurantDashboardController parent;

    private final MenuItemDAO menuItemDAO = new MenuItemDAO();

    // 🔑 CALLED FROM DASHBOARD
    public void setParent(RestaurantDashboardController parent) {
        this.parent = parent;
    }

    public void setItem(MenuItem item) {
        this.item = item;

        nameLabel.setText(item.getName());
        descLabel.setText(item.getDescription());
        priceLabel.setText(String.valueOf(item.getPrice())); // no $
        statusLabel.setText(item.isAvailable() ? "Available" : "Unavailable");
    }

    /* ================= BUTTON ACTIONS ================= */

    @FXML
    private void onEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demo2/edit-menu-item-popup.fxml")
            );

            Parent root = loader.load();

            EditMenuItemController controller = loader.getController();
            controller.setData(item, parent);

            Stage stage = new Stage();
            stage.setTitle("Edit Menu Item");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDelete() {
        System.out.println("Deleting item id = " + item.getItemId());
        // example delete logic (we'll formalize it next)
        menuItemDAO.deleteMenuItem(item.getItemId());

        // 🔁 refresh dashboard after delete
        if (parent != null) {
            parent.refreshMenu();
        }
    }
}
