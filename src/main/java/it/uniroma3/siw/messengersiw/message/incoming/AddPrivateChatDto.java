package it.uniroma3.siw.messengersiw.message.incoming;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Data transfer object
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@NoArgsConstructor
public class AddPrivateChatDto {

    private String username;
}
