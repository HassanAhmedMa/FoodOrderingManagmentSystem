package model.factory;


import model.user.*;


public class UserFactory {
    public static User createUser(String type, int id, String name, String email,String phone, String role) {
        switch (type.toUpperCase()) {
            case "CUSTOMER": return new Customer(id, name, email, phone, role);
            //case "RESTAURANT": return new RestaurantOwner(id, name, email);
            case "DELIVERY": return new DeliveryStaff(id, name, email,phone, role);
            default: throw new IllegalArgumentException("Invalid user type");
        }
    }
}