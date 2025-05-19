package app.calendar_service.web.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import app.calendar_service.application.security.services.JwtService;
import app.calendar_service.web.dtos.UserDto;
import app.calendar_service.web.security.dao.AuthenticationResponse;
import app.calendar_service.web.security.dao.CustomUserDetails;
import app.calendar_service.web.security.dao.SimpleAuthRecord;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static app.calendar_service.web.security.config.JwtEnv.*;

public class UserLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserLoginFilter(AuthenticationManager authManager, JwtService jwtService) {
        this.authenticationManager = authManager;
        this.jwtService = jwtService;

        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        SimpleAuthRecord user;
        String email = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), SimpleAuthRecord.class);
            email = user.email();
            password = user.password();

        } catch ( IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,password,new ArrayList<>());

        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();

        String jasonWebToken = jwtService.generateToken(userDetails);

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token(PREFIX_TOKEN + jasonWebToken)
                .uid(userDetails.getId())
                .username(userDetails.getUsername())
                .message(String.format("Hola %s has iniciado sesión con exito", userDetails.getUsername()))
                .build();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + jasonWebToken);

        response.getWriter().write((new ObjectMapper()).writeValueAsString(authResponse));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .error(failed.getMessage())
                .message("Error en la Autenticación, nombre de usuario o contraseña incorrectos.")
                .build();

        String body = (new ObjectMapper()).writeValueAsString(authResponse);

        response.getWriter().write(body);
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
