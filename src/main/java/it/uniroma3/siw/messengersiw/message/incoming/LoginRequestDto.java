package it.uniroma3.siw.messengersiw.message.incoming;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Data transfer object
 *
 * @author Mattia Micaloni
 */
@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @NotNull(message = "mandatory_username")
    private String username;

    @NotNull(message = "mandatory_password")
    private String password;
}
