package model.user;


public class DeliveryStaff extends User {
    public DeliveryStaff(int id, String name, String email) {
        super(id, name, email);
    }


    public String getRole() { return "DELIVERY_STAFF"; }
}