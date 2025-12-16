package dao;

import com.example.demo2.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void createUser(
            String fullName,
            String email,
            String passwordHash,
            String role,
            String phone
    ) {

        String sql =
                "INSERT INTO users (full_name, email, password_hash, role, phone) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, passwordHash);
            stmt.setString(4, role);
            stmt.setString(5, phone);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public ResultSet getUserByEmail(String email) {
//
//        String sql = "SELECT * FROM users WHERE email = ?";
//
//        try {
//            Connection conn = DBConnection.getConnection();
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setString(1, email);
//            return stmt.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    // ✅ Duplicate check: email exists
    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Duplicate check: phone exists
    public boolean phoneExists(String phone) {
        String sql = "SELECT 1 FROM users WHERE phone = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Login helper: get stored password + role + name (returns null if not found)
    public UserLoginData getLoginDataByEmail(String email) {
        String sql = "SELECT full_name, password_hash, role FROM users WHERE email = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserLoginData(
                            rs.getString("full_name"),
                            rs.getString("password_hash"),
                            rs.getString("role")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ✅ small DTO class used only for login
    public static class UserLoginData {
        private final String fullName;
        private final String password;
        private final String role;

        public UserLoginData(String fullName, String password, String role) {
            this.fullName = fullName;
            this.password = password;
            this.role = role;
        }

        public String getFullName() { return fullName; }
        public String getPassword() { return password; }
        public String getRole() { return role; }
    }
}
