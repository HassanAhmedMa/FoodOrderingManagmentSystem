package model.restaurant;

public class MenuItem {
    private int itemId;
    private int restaurantId;
    private String name;
    private String description;
    private double price;
    public MenuItem(
            int restaurantId,
            String name,
            String description,
            double price,
            String category,
            boolean available
    ) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    private String category;
    private String imageUrl;
    private boolean available;

    public MenuItem(int restaurantId, String name, double price) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
    }
    public int getItemId() {
        return itemId;
    }
    public MenuItem(int itemId, int restaurantId, String name, double price) {
        this.itemId = itemId;
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
