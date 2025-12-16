package model.user;

/**
 * Domain model for users table
 * NO database logic here
 */
public abstract class User {

    protected int id;
    protected String fullName;
    protected String email;
    protected String phone;
    protected String role;

    protected User(
            int id,
            String fullName,
            String email,
            String phone,
            String role
    ) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    /* ======================
       Getters
       ====================== */

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }
}
