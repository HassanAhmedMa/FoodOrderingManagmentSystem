package model.cart;

import model.order.Order;
import model.order.OrderItem;
import model.restaurant.MenuItem;

public class CartService {

    private static CartService instance;
    private final Order currentOrder;

    private CartService() {
        currentOrder = new Order(1, null, null); // userId later
    }

    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    public Order getOrder() {
        return currentOrder;
    }

    public void addItem(MenuItem menuItem) {
        for (OrderItem item : currentOrder.getItems()) {
            if (item.getItem().getItemId() == menuItem.getItemId()) {
                item.increaseQuantity();
                return;
            }
        }
        currentOrder.addItem(new OrderItem(menuItem, 1));
    }

    public void clear() {
        currentOrder.getItems().clear();
    }
}
