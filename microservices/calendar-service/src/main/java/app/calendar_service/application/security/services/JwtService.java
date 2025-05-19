package app.calendar_service.application.security.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import app.calendar_service.web.dtos.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static app.calendar_service.web.security.config.JwtEnv.*;

@Service
public class JwtService {

    public String generateToken(UserDetails user) throws JsonProcessingException {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        Claims claims = Jwts.claims()
                .add("username", user.getUsername())
                .add("authorities", new ObjectMapper().writeValueAsString(authorities))
                .build();

        return createToken(claims, user.getUsername());
    }

    public Boolean validateToken(Claims tokenClaims) {
        Date expirationDate = extractExpiration(tokenClaims);

        return !expirationDate.before(new Date());
    }

    private Date extractExpiration(Claims tokenClaims) {
        return tokenClaims.getExpiration();
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }
}
