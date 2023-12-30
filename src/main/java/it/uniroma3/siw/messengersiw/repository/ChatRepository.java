package it.uniroma3.siw.messengersiw.repository;

import it.uniroma3.siw.messengersiw.model.Chat;
import it.uniroma3.siw.messengersiw.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Chat repository
 */
@Repository
public interface ChatRepository extends CrudRepository<Chat, Long> {

    Optional<Chat> findByName(String name);

    List<Chat> findAllByMembers(User user);
}
