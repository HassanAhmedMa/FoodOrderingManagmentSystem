package model.validation.signup;

public abstract class BaseSignupHandler implements SignupHandler {

    private SignupHandler next;

    @Override
    public SignupHandler setNext(SignupHandler next) {
        this.next = next;
        return next; // fluent chaining
    }

    protected void next(SignupRequest request) throws SignupValidationException {
        if (next != null) next.handle(request);
    }
}
