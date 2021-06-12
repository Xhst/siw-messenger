package it.uniroma3.siw.messengersiw.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import java.util.Set;

/**
 * @author Mattia Micaloni
 */
@Data
@Entity
@NoArgsConstructor
@DiscriminatorValue("1")
public class GroupChat extends Chat {

    @ManyToOne
    private User owner;


    public GroupChat(String name, User owner, Set<User> members) {
        super(name);

        this.owner = owner;
        this.setMembers(members);
        this.addMember(owner);
    }
}
