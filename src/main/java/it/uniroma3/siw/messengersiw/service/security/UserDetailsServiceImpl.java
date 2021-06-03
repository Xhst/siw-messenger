package it.uniroma3.siw.messengersiw.service.security;

import it.uniroma3.siw.messengersiw.model.User;
import it.uniroma3.siw.messengersiw.model.UserDetailsImpl;
import it.uniroma3.siw.messengersiw.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mattia Micaloni
 */
@AllArgsConstructor
@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }
}
