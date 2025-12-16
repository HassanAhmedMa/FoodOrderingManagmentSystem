package model.validation.signup;

public class NameValidationHandler extends BaseSignupHandler {

    @Override
    public void handle(SignupRequest r) throws SignupValidationException {
        if (isEmpty(r.firstName))
            throw new SignupValidationException("firstName", "First name is required.");
        if (isEmpty(r.lastName))
            throw new SignupValidationException("lastName", "Last name is required.");

        next(r);
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
