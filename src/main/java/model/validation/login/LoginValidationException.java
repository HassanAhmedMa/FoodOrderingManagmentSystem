package model.validation.login;

public class LoginValidationException extends Exception {
    private final String fieldKey;

    public LoginValidationException(String fieldKey, String message) {
        super(message);
        this.fieldKey = fieldKey;
    }

    public String getFieldKey() {
        return fieldKey;
    }
}