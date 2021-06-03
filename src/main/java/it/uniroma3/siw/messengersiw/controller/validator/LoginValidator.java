package it.uniroma3.siw.messengersiw.controller.validator;

import it.uniroma3.siw.messengersiw.message.incoming.LoginRequestDto;
import it.uniroma3.siw.messengersiw.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Mattia Micaloni
 */
@Component
@AllArgsConstructor
public class LoginValidator implements Validator {

    private final UserService userService;


    @Override
    public boolean supports(Class<?> aClass) {
        return LoginRequestDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        LoginRequestDto request = (LoginRequestDto) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", ErrorCode.EMPTY_USERNAME.toString());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", ErrorCode.EMPTY_PASSWORD.toString());

        try {
            this.userService.getUser(request.getUsername());
        } catch(Exception e) {
            errors.reject(ErrorCode.NON_EXISTENT_USER.toString());
        }
    }
}
