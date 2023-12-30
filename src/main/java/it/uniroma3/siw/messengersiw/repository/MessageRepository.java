package it.uniroma3.siw.messengersiw.repository;

import it.uniroma3.siw.messengersiw.model.Message;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Message repository
 */
@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByChatId(long chatId);
}
