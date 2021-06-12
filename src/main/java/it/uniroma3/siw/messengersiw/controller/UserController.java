package it.uniroma3.siw.messengersiw.controller;

import it.uniroma3.siw.messengersiw.message.incoming.LoginRequestDto;
import it.uniroma3.siw.messengersiw.message.incoming.RegisterRequestDto;
import it.uniroma3.siw.messengersiw.message.outgoing.JwtResponseDto;
import it.uniroma3.siw.messengersiw.message.outgoing.MessageResponseDto;
import it.uniroma3.siw.messengersiw.model.User;
import it.uniroma3.siw.messengersiw.security.UserDetailsImpl;
import it.uniroma3.siw.messengersiw.security.jwt.JwtUtils;
import it.uniroma3.siw.messengersiw.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


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
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final JwtUtils jwtUtils;


    /**
     *
     * @param request data transfer object
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
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
     *
     * @param request data transfer object
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto request) {
        this.userService.save(
            new User(request.getUsername(),
                    request.getEmail(),
                    this.passwordEncoder.encode(request.getPassword()))
        );

        return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
