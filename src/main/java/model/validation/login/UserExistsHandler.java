package model.validation.login;

import dao.UserDAO;

public class UserExistsHandler extends BaseLoginHandler {

    private final UserDAO userDAO;

    public UserExistsHandler(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void handle(LoginRequest r) throws LoginValidationException {
        UserDAO.UserLoginData data = userDAO.getLoginDataByEmail(r.email);

        if (data == null)
            throw new LoginValidationException("email", "No account found with this email.");

        r.loginData = data;

        next(r);
    }
}