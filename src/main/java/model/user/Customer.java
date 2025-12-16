package model.user;

public class Customer extends User {

    public Customer(int id, String name, String email,String phone, String role) {
        super(id, name, email,phone,role);
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }
}
