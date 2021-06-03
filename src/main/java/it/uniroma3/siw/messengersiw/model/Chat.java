package it.uniroma3.siw.messengersiw.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mattia Micaloni
 */
@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="isGroupChat",
        discriminatorType = DiscriminatorType.INTEGER)
public abstract class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy="chat")
    private List<Message> messages;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "chats_members",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> members;


    public Chat() {
        this.messages = new ArrayList<>();
        this.members = new HashSet<>();
    }

    public Chat(String name) {
        this();
        this.name = name;
    }

    public void addMember(User user) {
        if (user == null) return;

        this.members.add(user);
    }
}
