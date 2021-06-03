package it.uniroma3.siw.messengersiw.message.incoming;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    private String username;
    private String email;
    private String password;
}
