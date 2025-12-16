package model.validation.signup;

import java.util.regex.Pattern;

public class EmailValidationHandler extends BaseSignupHandler {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public void handle(SignupRequest r) throws SignupValidationException {
        if (isEmpty(r.email))
            throw new SignupValidationException("email", "Email is required.");

        if (!EMAIL_PATTERN.matcher(r.email).matches())
            throw new SignupValidationException("email", "Please enter a valid email.");

        next(r);
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
