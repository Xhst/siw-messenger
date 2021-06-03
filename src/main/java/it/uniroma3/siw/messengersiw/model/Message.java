package it.uniroma3.siw.messengersiw.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.Date;

/**
 * @author Mattia Micaloni
 */
@Data
@Entity
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column
    private Date sendDate;

    @ManyToOne
    private User sender;

    @ManyToOne
    private Chat chat;

    public Message(User sender, Chat chat, String text) {
        this.sender = sender;
        this.chat = chat;
        this.text = text;
        this.sendDate = new Date(System.currentTimeMillis());
    }
}
