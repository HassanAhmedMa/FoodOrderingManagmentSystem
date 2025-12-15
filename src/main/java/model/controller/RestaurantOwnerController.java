package model.controller;

import model.order.Order;
import model.restaurant.MenuItem;
import model.restaurant.Restaurant;

public class RestaurantOwnerController {

    public void addMenuItem(Restaurant restaurant, MenuItem item) {
        restaurant.addItem(item);
    }

    public void acceptOrder(Order order) {
        System.out.println("Owner accepted order #" + order.getId());
        order.nextState();
    }
}
