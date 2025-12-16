package model.validation.login;

import java.util.regex.Pattern;

public class EmailValidationHandler extends BaseLoginHandler {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public void handle(LoginRequest r) throws LoginValidationException {
        if (isEmpty(r.email))
            throw new LoginValidationException("email", "Email is required.");

        if (!EMAIL_PATTERN.matcher(r.email).matches())
            throw new LoginValidationException("email", "Please enter a valid email.");

        next(r);
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}