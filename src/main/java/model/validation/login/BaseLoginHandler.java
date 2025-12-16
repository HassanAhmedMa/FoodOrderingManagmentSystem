package model.validation.login;

public abstract class BaseLoginHandler implements LoginHandler {

    private LoginHandler next;

    @Override
    public LoginHandler setNext(LoginHandler next) {
        this.next = next;
        return next;
    }

    protected void next(LoginRequest request) throws LoginValidationException {
        if (next != null) next.handle(request);
    }
}
