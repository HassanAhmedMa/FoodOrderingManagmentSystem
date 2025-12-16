package model.user;

public class RestaurantOwner {

    private int id;
    private String name;
    private String email;

    // FULL constructor
    public RestaurantOwner(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // 🔥 REQUIRED FOR DAO
    public RestaurantOwner(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}