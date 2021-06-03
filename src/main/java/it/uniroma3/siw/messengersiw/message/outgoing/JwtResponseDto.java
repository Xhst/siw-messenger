package it.uniroma3.siw.messengersiw.message.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data transfer object
 *
 * @author Mattia Micaloni
 */
@Getter
@AllArgsConstructor
public class JwtResponseDto {
    private String token;
    private Long id;
    private String username;
    private String email;
}
