package model.validation.login;

public class LoginRequiredFieldsHandler extends BaseLoginHandler {

    @Override
    public void handle(LoginRequest r) throws LoginValidationException {
        if (isEmpty(r.email)) {
            throw new LoginValidationException("email", "Email is required.");
        }
        if (isEmpty(r.password)) {
            throw new LoginValidationException("password", "Password is required.");
        }
        next(r);
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
