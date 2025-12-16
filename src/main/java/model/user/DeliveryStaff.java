package model.user;


public class DeliveryStaff extends User {
    public DeliveryStaff(int id, String name, String email, String phone, String role) {
        super(id, name, email,phone,role);
    }


    public String getRole() { return "DELIVERY_STAFF"; }
}