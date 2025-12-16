package model.restaurant;

import model.user.RestaurantOwner;

public class Restaurant {

    private int id;
    private String name;
    private String description;
    private String location;
    private double ratingAvg;
    private boolean isOpen;
    private String imageUrl;          // 🔥 NEW
    private RestaurantOwner owner;

    public Restaurant(int id,
                      String name,
                      String description,
                      String location,
                      double ratingAvg,
                      boolean isOpen,
                      String imageUrl,       // 🔥 NEW
                      RestaurantOwner owner) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.ratingAvg = ratingAvg;
        this.isOpen = isOpen;
        this.imageUrl = imageUrl;
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
    public String getImageUrl() { return imageUrl; } // 🔥 NEW
    public RestaurantOwner getOwner() { return owner; }
}
