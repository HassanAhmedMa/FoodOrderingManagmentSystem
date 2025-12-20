package model.validation.signup;

import java.util.regex.Pattern;

public class PhoneValidationHandler extends BaseSignupHandler {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^01\\d{9}$");

    @Override
    public void handle(SignupRequest r) throws SignupValidationException {
        if (isEmpty(r.phone))
            throw new SignupValidationException("phone", "Phone number is required.");

        if (!PHONE_PATTERN.matcher(r.phone).matches())
            throw new SignupValidationException("phone", "Phone must be 11 digits and start with 01.");

        next(r);
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
