package model.validation.login;

import dao.UserDAO;

public class LoginRequest {
    public String email;
    public String password;

    // filled by UserExistsHandler
    public UserDAO.UserLoginData loginData;
}