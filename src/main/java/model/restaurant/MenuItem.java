package model.restaurant;


public class MenuItem {
    private int restaurantId;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
    private boolean available;

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public MenuItem(int id, String name, double price) {
        this.restaurantId = id;
        this.name = name;
        this.price = price;
    }


    public double getPrice() { return price; }
    public String getName() { return name; }
}