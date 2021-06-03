package it.uniroma3.siw.messengersiw.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Mattia Micaloni
 */
@Data
@Entity
@NoArgsConstructor
@DiscriminatorValue("0")
public class PrivateChat extends Chat {

    public PrivateChat(User user1, User user2) {
        super(nameFromUsernames(user1.getUsername(), user2.getUsername()));

        super.addMember(user1);
        super.addMember(user2);
    }

    @Override
    public void addMember(User user) { }

    public static String nameFromUsernames(String username1, String username2) {
        return username1.compareTo(username2) <= 0
                ? username1 + "-" + username2
                : username2 + "-" + username1;
    }
}
