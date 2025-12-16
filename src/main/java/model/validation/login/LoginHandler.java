package model.validation.login;

public interface LoginHandler {
    LoginHandler setNext(LoginHandler next);
    void handle(LoginRequest request) throws LoginValidationException;
}
