package it.uniroma3.siw.messengersiw.controller.validator;

/**
 * @author Mattia Micaloni
 */
public enum ErrorCode {
    EMPTY_USERNAME("empty_username"),
    EMPTY_EMAIL("empty_email"),
    EMPTY_PASSWORD("empty_password"),
    USERNAME_TAKEN("username_taken"),
    INVALID_USERNAME("invalid_username"),
    INVALID_EMAIL("invalid_email"),
    INVALID_PASSWORD("invalid_password"),
    NON_EXISTENT_USER("non_existent_user"),
    PASSWORDS_NOT_MATCH("passwords_not_match");


    private final String text;

    ErrorCode(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
