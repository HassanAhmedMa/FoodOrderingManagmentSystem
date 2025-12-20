package model.validation.signup;

import java.util.regex.Pattern;

public class PasswordValidationHandler extends BaseSignupHandler {

    private static final Pattern PASS_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    @Override
    public void handle(SignupRequest r) throws SignupValidationException {
        if (isEmpty(r.password))
            throw new SignupValidationException("password", "Password is required.");

        if (!PASS_PATTERN.matcher(r.password).matches())
            throw new SignupValidationException(
                    "password",
                    "Password must be 8+ chars with uppercase, lowercase, and number."
            );

        if (isEmpty(r.confirmPassword))
            throw new SignupValidationException("confirmPassword", "Please confirm your password.");

        if (!r.password.equals(r.confirmPassword))
            throw new SignupValidationException("confirmPassword", "Passwords do not match.");

        next(r);
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
