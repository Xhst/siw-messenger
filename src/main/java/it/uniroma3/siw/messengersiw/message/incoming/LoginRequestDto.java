package it.uniroma3.siw.messengersiw.message.incoming;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data transfer object
 *
 * @author Mattia Micaloni
 */
@Getter
@NoArgsConstructor
public class LoginRequestDto {

    private String username;
    private String password;
}
