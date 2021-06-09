package it.uniroma3.siw.messengersiw.service;

import it.uniroma3.siw.messengersiw.model.User;
import it.uniroma3.siw.messengersiw.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * @author Mattia Micaloni
 */
@AllArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;


    public User getUser(String username) throws NoSuchElementException {
        return this.userRepository.findByUsername(username)
                .orElse(null);
    }

    public boolean existsWithUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public User save(User user) {
        return this.userRepository.save(user);
    }

}
