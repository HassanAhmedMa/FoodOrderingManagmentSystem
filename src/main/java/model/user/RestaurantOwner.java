package model.user;


public class RestaurantOwner extends User {
    public RestaurantOwner(int id, String name, String email) {
        super(id, name, email);
    }


    public String getRole() { return "RESTAURANT_OWNER"; }
}