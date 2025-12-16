package model.restaurant;

import model.user.RestaurantOwner;

public class Restaurant {

    private int id;
    private String name;
    private String description;
    private String location;
    private double ratingAvg;
    private boolean isOpen;
    private RestaurantOwner owner;

    public Restaurant(int id, String name, String description,
                      String location, double ratingAvg,
                      boolean isOpen, RestaurantOwner owner) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.ratingAvg = ratingAvg;
        this.isOpen = isOpen;
        this.owner = owner;
    }
    public Restaurant(int id) {
        this.id = id;
    }


    /* ================= GETTERS ================= */

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public double getRatingAvg() { return ratingAvg; }
    public boolean isOpen() { return isOpen; }
    public RestaurantOwner getOwner() { return owner; }

    public void addItem(MenuItem item) {
    }
}