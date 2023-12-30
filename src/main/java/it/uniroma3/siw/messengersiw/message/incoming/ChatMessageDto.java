package it.uniroma3.siw.messengersiw.message.incoming;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Data transfer object
 */
@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
public class ChatMessageDto {

    private String fromUser;
    private long toChat;
    private String message;
}
