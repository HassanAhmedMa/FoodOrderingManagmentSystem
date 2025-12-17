package model.cart;

import model.order.OrderItem;
import model.restaurant.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CartService {

    private static CartService instance;

    private final List<OrderItem> items = new ArrayList<>();

    private CartService() {}

    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    /* ================= ADD ================= */

    public void addItem(MenuItem menuItem) {
        for (OrderItem item : items) {
            if (item.getItem().getItemId() == menuItem.getItemId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        items.add(new OrderItem(menuItem, 1));
    }

    /* ================= READ ================= */

    public List<OrderItem> getItems() {
        return items;
    }

    public int getTotalItemsCount() {
        return items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    /* ================= CLEAR ================= */

    public void clear() {
        items.clear();
    }
}
