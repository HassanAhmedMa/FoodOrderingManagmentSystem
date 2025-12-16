package model.validation.signup;

public interface SignupHandler {
    SignupHandler setNext(SignupHandler next);
    void handle(SignupRequest request) throws SignupValidationException;
}
