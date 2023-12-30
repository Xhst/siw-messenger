package it.uniroma3.siw.messengersiw.repository;

import it.uniroma3.siw.messengersiw.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * User repository
 */
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

}
