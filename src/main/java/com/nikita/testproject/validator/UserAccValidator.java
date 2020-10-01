package com.nikita.testproject.validator;

import com.nikita.testproject.request.UserAccRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserAccValidator implements Validator {


    private static final int MINIMUM_PASSWORD_LENGTH = 6;
    private static final int MINIMUM_LOGIN_LENGTH = 6;

    public boolean supports(Class clazz) {
        return UserAccRequest.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
        UserAccRequest request = (UserAccRequest) target;

        if (request.getLogin() != null
                && request.getLogin().trim().length() < MINIMUM_LOGIN_LENGTH) {
            errors.rejectValue("login", "field.min.length",
                    new Object[]{MINIMUM_PASSWORD_LENGTH},
                    "The login must be at least [" + MINIMUM_PASSWORD_LENGTH + "] characters in length.");
        }
        if (request.getPassword() != null
                && request.getPassword().trim().length() < MINIMUM_PASSWORD_LENGTH) {
            errors.rejectValue("password", "field.min.length",
                    new Object[]{MINIMUM_PASSWORD_LENGTH},
                    "The password must be at least [" + MINIMUM_PASSWORD_LENGTH + "] characters in length.");
        }

    }
}