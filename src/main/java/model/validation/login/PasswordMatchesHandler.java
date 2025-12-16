package model.validation.login;

public class PasswordMatchesHandler extends BaseLoginHandler {

    @Override
    public void handle(LoginRequest r) throws LoginValidationException {
        if (!r.password.equals(r.loginData.getPassword()))
            throw new LoginValidationException("password", "Incorrect password.");

        next(r);
    }
}