package it.uniroma3.siw.messengersiw.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import it.uniroma3.siw.messengersiw.security.UserDetailsImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Json Web Token utils.
 * It's used for generate or validate the token or to retrieve information contained in the subject.
 *
 * @author Mattia Micaloni
 */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${messengerSiw.jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${messengerSiw.jwt.expirationMs}")
    private int jwtExpirationMs;

    private Jws<Claims> decode(String token) {
        return Jwts.parser().setSigningKey(this.jwtSecretKey).parseClaimsJws(token);
    }

    /**
     * Get the username contained in the JWT's subject
     *
     * @param token JWT
     * @return username
     */
    public String getUsernameFromToken(String token) {
        return this.decode(token)
                .getBody()
                .getSubject();
    }

    /**
     * Generate a Json Web Token from the given authentication.
     * It will be secured with SHA512 and a secret key given in the configuration.
     * The principal username will be stored in the subject.
     *
     * @param authentication Spring security authentication principal
     * @return JWT
     */
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, this.jwtSecretKey)
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + this.jwtExpirationMs))
                .compact();
    }

    /**
     * Check if the given token is valid or not.
     *
     * @param token JWT
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token) {
        try {
            this.decode(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}