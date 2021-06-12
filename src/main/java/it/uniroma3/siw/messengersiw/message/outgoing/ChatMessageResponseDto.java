package it.uniroma3.siw.messengersiw.message.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * Data transfer object
 *
 * @author Mattia Micaloni
 */
@Getter
@AllArgsConstructor
public class ChatMessageResponseDto {

    private final long id;
    private final String from;
    private final String text;
    private final Date sendDate;
}
