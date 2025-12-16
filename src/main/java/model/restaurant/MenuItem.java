package model.restaurant;

public class MenuItem {

    private int restaurantId;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
    private boolean available;

    public MenuItem(int restaurantId, int restaurant_id, String name, double price) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }
}
