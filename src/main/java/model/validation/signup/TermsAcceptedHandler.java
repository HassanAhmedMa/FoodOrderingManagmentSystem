package model.validation.signup;

public class TermsAcceptedHandler extends BaseSignupHandler {

    @Override
    public void handle(SignupRequest r) throws SignupValidationException {
        if (!r.acceptedTerms) {
            throw new SignupValidationException("terms", "You must agree to the terms.");
        }
        next(r);
    }
}