package model.validation.signup;

public class SignupValidationException extends Exception {
    private final String fieldKey;

    public SignupValidationException(String fieldKey, String message) {
        super(message);
        this.fieldKey = fieldKey;
    }

    public String getFieldKey() {
        return fieldKey;
    }
}
