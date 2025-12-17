package model.controller;

import dao.MenuItemDAO;
import model.order.Order;
import model.restaurant.MenuItem;
import model.restaurant.Restaurant;

public class RestaurantOwnerController {

    private final MenuItemDAO menuItemDAO = new MenuItemDAO();

    public void addMenuItem(Restaurant restaurant, MenuItem item) {

        // 🔥 set restaurant ID on item
        item.setRestaurantId(restaurant.getId());

        // 🔥 save to DB
        menuItemDAO.updateMenuItem(item);

        System.out.println("Menu item added to restaurant " + restaurant.getName());
    }

    public void acceptOrder(Order order) {
        System.out.println("Owner accepted order #" + order.getId());
        order.nextState();
    }
}
