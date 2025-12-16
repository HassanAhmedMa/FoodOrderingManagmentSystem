package model.validation.login;

public class PasswordRequiredHandler extends BaseLoginHandler {

    @Override
    public void handle(LoginRequest r) throws LoginValidationException {
        if (r.password == null || r.password.isEmpty())
            throw new LoginValidationException("password", "Password is required.");

        next(r);
    }
}