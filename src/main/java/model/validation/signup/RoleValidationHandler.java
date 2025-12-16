package model.validation.signup;

public class RoleValidationHandler extends BaseSignupHandler {

    @Override
    public void handle(SignupRequest r) throws SignupValidationException {
        if (r.role == null)
            throw new SignupValidationException("role", "Please select a role.");

        next(r);
    }
}
