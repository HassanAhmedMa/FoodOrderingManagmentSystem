package model.controller;

import com.example.demo2.Navigator;
import com.example.demo2.Session;
import dao.RestaurantDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.restaurant.Restaurant;
import model.user.RestaurantOwner;
import model.user.User;

public class RestaurantSetupController {

    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField locationField;

    private final RestaurantDAO restaurantDAO = new RestaurantDAO();

    @FXML
    private void initialize() {

        User owner = Session.getUser();

        // Safety check
        if (owner == null || !"RESTAURANT".equals(owner.getRole())) {
            Navigator.goTo("/com/example/demo2/Login.fxml");
            return;
        }

        if (restaurantDAO.getRestaurantByOwner(owner.getId()) != null) {
            Navigator.goTo("/com/example/demo2/restaurant-dashboard.fxml");
            return;
        }
    }
    @FXML
    private void onNext() {

        User user = Session.getUser();

        // 1️⃣ Must be logged in restaurant owner
        if (user == null || !"RESTAURANT".equals(user.getRole())) {
            System.out.println(" No restaurant user in session");
            return;
        }

        // 2️⃣ Basic validation (VERY IMPORTANT)
        if (nameField.getText().isBlank()
                || descriptionField.getText().isBlank()
                || locationField.getText().isBlank()) {

            System.out.println("Missing restaurant fields");
            return;
        }

        // 3️⃣ Create owner object
        RestaurantOwner owner = new RestaurantOwner(user.getId());

        // 4️⃣ Create restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);                    // 🔥 owner_id = user.id
        restaurant.setName(nameField.getText().trim());
        restaurant.setDescription(descriptionField.getText().trim());
        restaurant.setLocation(locationField.getText().trim());
        restaurant.setRatingAvg(0.0);
        restaurant.setOpen(true);
        restaurant.setImageUrl(null);

        // 5️⃣ Save to DB
        restaurantDAO.createRestaurant(restaurant);

        System.out.println("✅ Restaurant created for user ID: " + user.getId());

        // 6️⃣ Go to dashboard
        Navigator.goTo("/com/example/demo2/restaurant-dashboard.fxml");
    }


}
