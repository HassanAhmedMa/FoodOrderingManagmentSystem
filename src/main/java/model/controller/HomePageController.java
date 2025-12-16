package model.controller;

import com.example.demo2.Navigator;
import javafx.fxml.FXML;

public class HomePageController {

    @FXML
    private void onBrowseClicked() {
        Navigator.goTo("/com/example/demo2/browse-restaurants.fxml");
    }
}
