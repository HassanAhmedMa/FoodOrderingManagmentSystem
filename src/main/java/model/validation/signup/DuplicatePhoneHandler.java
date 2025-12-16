package model.validation.signup;

import dao.UserDAO;

public class DuplicatePhoneHandler extends BaseSignupHandler {

    private final UserDAO userDAO;

    public DuplicatePhoneHandler(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void handle(SignupRequest r) throws SignupValidationException {
        if (userDAO.phoneExists(r.phone))
            throw new SignupValidationException("phone", "This phone number is already registered.");

        next(r);
    }
}
