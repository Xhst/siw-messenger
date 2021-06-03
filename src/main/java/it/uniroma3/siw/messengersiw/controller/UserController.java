package it.uniroma3.siw.messengersiw.controller;

import it.uniroma3.siw.messengersiw.controller.validator.LoginValidator;
import it.uniroma3.siw.messengersiw.controller.validator.RegisterValidator;
import it.uniroma3.siw.messengersiw.message.incoming.LoginRequestDto;
import it.uniroma3.siw.messengersiw.message.incoming.RegisterRequestDto;
import it.uniroma3.siw.messengersiw.message.outgoing.JwtResponseDto;
import it.uniroma3.siw.messengersiw.message.outgoing.MessageResponseDto;
import it.uniroma3.siw.messengersiw.model.User;
import it.uniroma3.siw.messengersiw.model.UserDetailsImpl;
import it.uniroma3.siw.messengersiw.service.security.jwt.JwtUtils;
import it.uniroma3.siw.messengersiw.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    private final LoginValidator loginValidator;
    private final RegisterValidator registerValidator;

    private final JwtUtils jwtUtils;


    /**
     *
     * @param request data transfer object
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        Errors result = new BeanPropertyBindingResult(request, request.getClass().getName());
        this.loginValidator.validate(request, result);

        if (result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDto("Errors: "+ result.getGlobalError()));
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
     *
     * @param request data transfer object
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
        Errors result = new BeanPropertyBindingResult(request, request.getClass().getName());
        this.registerValidator.validate(request, result);

        if (result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDto("Errors: " + result.getAllErrors()));
        }

        this.userService.save(
            new User(request.getUsername(),
                    request.getEmail(),
                    this.passwordEncoder.encode(request.getPassword()))
        );

        return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
    }
}
