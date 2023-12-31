package it.uniroma3.siw.messengersiw.message.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data transfer object
 */
@Getter
@AllArgsConstructor
public class AuthenticationResponseDto {

    private final String token;
    private final String username;
}
