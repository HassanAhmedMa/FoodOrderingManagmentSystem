package model.restaurant;

import model.user.RestaurantOwner;
import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private int id;
    private String name;
    private RestaurantOwner owner;
    private List<MenuItem> menu = new ArrayList<>();

    public Restaurant(int id, String name, RestaurantOwner owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public void addItem(MenuItem item) {
        menu.add(item);
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public String getName() {
        return name;
    }

    public RestaurantOwner getOwner() {
        return owner;
    }
}
