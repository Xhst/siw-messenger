package it.uniroma3.siw.messengersiw.controller.validator;

import it.uniroma3.siw.messengersiw.message.incoming.RegisterRequestDto;
import it.uniroma3.siw.messengersiw.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

/**
 * @author Mattia Micaloni
 */
@Component
@AllArgsConstructor
public class RegisterValidator implements Validator {

    private final UserService userService;


    @Override
    public boolean supports(Class<?> aClass) {
        return RegisterRequestDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegisterRequestDto request = (RegisterRequestDto) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", ErrorCode.EMPTY_USERNAME.toString());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", ErrorCode.EMPTY_USERNAME.toString());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", ErrorCode.EMPTY_PASSWORD.toString());

        if (this.userService.existsWithUsername(request.getUsername())) {
            errors.reject(ErrorCode.USERNAME_TAKEN.toString());
        }

        if (!this.isUsernameValid(request.getUsername())) {
            errors.reject(ErrorCode.INVALID_USERNAME.toString());
        }

        if (!this.isPasswordValid(request.getPassword())) {
            errors.reject(ErrorCode.INVALID_PASSWORD.toString());
        }
    }

    /**
     * Validate username: username is 3-10 characters long, allowed characters: [a-z], [A-Z], [0-9].
     *
     * @param username username that needs validation.
     * @return true if the given username is valid, false otherwise.
     */
    private boolean isUsernameValid(String username) {
        return Pattern.compile("(?=.{3,10}$)[a-zA-Z0-9]*$")
                .matcher(username)
                .matches();
    }

    /**
     * Validate password: min 6 chars, max 100 chars, at least 1 number.
     *
     * @param password password that needs validation.
     * @return true if the given password is valid, false otherwise.
     */
    private boolean isPasswordValid(String password) {
        return Pattern.compile("^(?=.{6,100}$).*[0-9].*[a-zA-Z0-9._-]$")
                .matcher(password)
                .matches();
    }
}
