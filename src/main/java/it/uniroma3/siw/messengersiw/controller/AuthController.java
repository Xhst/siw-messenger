package it.uniroma3.siw.messengersiw.controller;

import it.uniroma3.siw.messengersiw.message.incoming.LoginRequestDto;
import it.uniroma3.siw.messengersiw.message.incoming.RegisterRequestDto;
import it.uniroma3.siw.messengersiw.message.outgoing.JwtResponseDto;
import it.uniroma3.siw.messengersiw.message.outgoing.SuccessResponseDto;
import it.uniroma3.siw.messengersiw.model.User;
import it.uniroma3.siw.messengersiw.security.UserDetailsImpl;
import it.uniroma3.siw.messengersiw.security.jwt.JwtUtils;
import it.uniroma3.siw.messengersiw.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * Rest controller for user authentication.
 * Contains methods for handling HTTP requests.
 *
 * @author Mattia Micaloni
 */
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final JwtUtils jwtUtils;


    /**
     * Login user
     * @param request data transfer object
     * @return ResponseEntity with JWT and user data
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {

        if (!this.userService.existsWithUsername(request.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.jwtUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponseDto(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail())
        );
    }

    /**
     * Register user
     * @param request data transfer object
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto request) {

        if (this.userService.existsWithUsername(request.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        this.userService.save(
            new User(request.getUsername(),
                    request.getEmail(),
                    this.passwordEncoder.encode(request.getPassword()))
        );

        return ResponseEntity.ok(new SuccessResponseDto("register_success"));
    }
}
