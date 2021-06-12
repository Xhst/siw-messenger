package it.uniroma3.siw.messengersiw.message.incoming;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Data transfer object
 *
 * @author Mattia Micaloni
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
public class RegisterRequestDto {

    /**
     * Register Username
     * Cannot be null, min 3 chars, max 15 chars, allowed characters: [a-z], [A-Z], [0-9].
     */
    @NotNull(message = "mandatory_username")
    @Size(min = 3, max = 15, message = "size_username")
    @Pattern(regexp = "(?=.{3,10}$)[a-zA-Z0-9]*$", message = "invalid_username")
    private String username;

    /**
     * Register E-mail
     */
    @Email(message = "invalid_email")
    private String email;

    /**
     *  Register password
     *  Cannot be null, min 6 chars, max 100 chars, at least 1 number.
     */
    @NotNull(message = "mandatory_password")
    @Size(min = 6, max = 100, message = "size_password")
    @Pattern(regexp = "^(?=.{6,100}$).*[0-9].*[a-zA-Z0-9._-]$", message = "invalid_password")
    private String password;
}
