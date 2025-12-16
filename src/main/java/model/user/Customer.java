package model.user;

public class Customer extends User {

    public Customer(int id, String name, String email, String phone, String role) {
        super(id, name, email, phone, role);
    }

    public Customer(int id, String name) {
        super(id, name, null, null, "CUSTOMER");
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }

    public String getName() {
        return fullName;
    }
}
