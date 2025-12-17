package model.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.cart.CartService;
import model.restaurant.MenuItem;

import java.net.URL;

public class MenuItemCardController {

    @FXML private Label itemName;
    @FXML private Label itemDescription;
    @FXML private Label itemPrice;
    @FXML private ImageView itemImage;
    @FXML private Button addButton;

    private MenuItem menuItem;

    public void setData(MenuItem menuItem) {
        this.menuItem = menuItem;

        itemName.setText(menuItem.getName());
        itemDescription.setText(menuItem.getDescription());
        itemPrice.setText(String.format("%.2f $", menuItem.getPrice()));

        loadImage(menuItem.getImageUrl());
    }

    @FXML
    private void handleAdd() {
        if (menuItem == null) {
            System.out.println("❌ MenuItem is null");
            return;
        }

        CartService.getInstance().addItem(menuItem);

        System.out.println("✅ Added to cart: " + menuItem.getName());
    }

    private void loadImage(String path) {
        if (path == null || path.isBlank()) return;

        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                itemImage.setImage(new Image(url.toExternalForm(), true));
            }
        } catch (Exception ignored) {}
    }
}
