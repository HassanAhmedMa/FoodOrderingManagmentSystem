package model.controller;

import com.example.demo2.Navigator;
import com.example.demo2.Session;
import dao.RestaurantDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.restaurant.Restaurant;
import model.user.RestaurantOwner;
import model.user.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class RestaurantSetupController {

    /* ================= FXML ================= */

    @FXML private ImageView previewImage;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField locationField;

    /* ================= STATE ================= */

    private File selectedImageFile;
    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    /* ================= INIT ================= */

    @FXML
    private void initialize() {

        User owner = Session.getUser();

        if (owner == null || !"RESTAURANT".equals(owner.getRole())) {
            Navigator.goTo("/com/example/demo2/login.fxml");
            return;
        }

        // If owner already has a restaurant → skip setup
        if (restaurantDAO.getRestaurantByOwner(owner.getId()) != null) {
            Navigator.goTo("/com/example/demo2/restaurant-dashboard.fxml");
        }
    }

    /* ================= IMAGE PICKER ================= */

    @FXML
    private void onChooseImage() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Restaurant Image");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Images", "*.png", "*.jpg", "*.jpeg"
                )
        );

        File file = chooser.showOpenDialog(nameField.getScene().getWindow());
        if (file == null) return;

        selectedImageFile = file;

        // Preview immediately
        previewImage.setImage(new Image(file.toURI().toString()));
    }

    /* ================= NEXT ================= */

    @FXML
    private void onNext() {

        User user = Session.getUser();

        if (user == null || !"RESTAURANT".equals(user.getRole())) {
            return;
        }

        if (nameField.getText().isBlank()
                || descriptionField.getText().isBlank()
                || locationField.getText().isBlank()) {
            return;
        }

        RestaurantOwner owner = new RestaurantOwner(user.getId());

        // Save image if chosen
        String imagePath = null;
        if (selectedImageFile != null) {
            imagePath = saveImageToApp(selectedImageFile);
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);
        restaurant.setName(nameField.getText().trim());
        restaurant.setDescription(descriptionField.getText().trim());
        restaurant.setLocation(locationField.getText().trim());
        restaurant.setRatingAvg(0.0);
        restaurant.setOpen(true);
        restaurant.setImageUrl(imagePath);

        restaurantDAO.createRestaurant(restaurant);

        Navigator.goTo("/com/example/demo2/restaurant-dashboard.fxml");
    }

    /* ================= IMAGE SAVE ================= */

    private String saveImageToApp(File imageFile) {

        try {
            // ✅ External folder (NOT classpath)
            Path imagesDir = Path.of("user-data/images/restaurants");
            Files.createDirectories(imagesDir);

            String fileName = System.currentTimeMillis() + "_" + imageFile.getName();
            Path destination = imagesDir.resolve(fileName);

            Files.copy(
                    imageFile.toPath(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // ✅ Save ABSOLUTE path in DB
            return destination.toAbsolutePath().toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
