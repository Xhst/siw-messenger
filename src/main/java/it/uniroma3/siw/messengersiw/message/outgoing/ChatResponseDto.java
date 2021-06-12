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
public class ChatResponseDto {

    private final long id;
    private final String name;
    private final boolean isGroupChat;
    private final String ownerName;

}
