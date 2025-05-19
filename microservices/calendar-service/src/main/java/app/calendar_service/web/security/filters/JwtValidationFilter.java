package app.calendar_service.web.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import app.calendar_service.application.security.services.JwtService;
import app.calendar_service.web.security.dao.SimpleGrantedAuthorityJsonCreator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static app.calendar_service.web.security.config.JwtEnv.*;

@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(PREFIX_TOKEN)){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace(PREFIX_TOKEN,"");

        Claims claims = Jwts.parser().verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String subject = claims.getSubject();

        if(!jwtService.validateToken(claims)) {

            response.getWriter().write((new ObjectMapper()).writeValueAsString(
                    Map.of(
                            "message", "El token de sesión no es válido"
                    )));
            response.setContentType(CONTENT_TYPE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        Collection<? extends GrantedAuthority> authorities = List.of(
                (new ObjectMapper())
                        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                        .readValue(claims.get("authorities").toString().getBytes(), SimpleGrantedAuthority[].class)
        );

        UsernamePasswordAuthenticationToken upaToken = new UsernamePasswordAuthenticationToken(
                subject,
                null,
                authorities);

        upaToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(upaToken);

        filterChain.doFilter(request, response);
    }
}
