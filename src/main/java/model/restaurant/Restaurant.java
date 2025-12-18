package model.restaurant;

import model.user.RestaurantOwner;

public class Restaurant {

    private int id;
    private String name;
    private String description;
    private String location;
    private double ratingAvg;
    private boolean isOpen;
    private String imageUrl;
    private RestaurantOwner owner;

    // ✅ REQUIRED for controllers / DAOs
    public Restaurant() {}

    // Used by DAO when reading from DB
    public Restaurant(int id,
                      String name,
                      String description,
                      String location,
                      double ratingAvg,
                      boolean isOpen,
                      String imageUrl,
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
    public String getImageUrl() { return imageUrl; }
    public RestaurantOwner getOwner() { return owner; }

    /* ================= SETTERS ================= */

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setRatingAvg(double ratingAvg) { this.ratingAvg = ratingAvg; }
    public void setOpen(boolean open) { isOpen = open; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setOwner(RestaurantOwner owner) { this.owner = owner; }
}
