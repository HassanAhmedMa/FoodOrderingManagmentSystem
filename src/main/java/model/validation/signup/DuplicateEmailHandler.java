package model.validation.signup;

import dao.UserDAO;

public class DuplicateEmailHandler extends BaseSignupHandler {

    private final UserDAO userDAO;

    public DuplicateEmailHandler(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void handle(SignupRequest r) throws SignupValidationException {
        if (userDAO.emailExists(r.email))
            throw new SignupValidationException("email", "This email is already registered.");

        next(r);
    }
}
