package app.calendar_service.application.security.services;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.UserRepository;
import app.calendar_service.domain.entities.User;
import app.calendar_service.web.security.dao.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findOneByEmailWithAuthorities(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario: %s no existe."));

        List<GrantedAuthority> grantedAuthorities = foundUser.getAuthorities().stream()
                .map((auth) -> new SimpleGrantedAuthority(auth.getName()))
                .collect(Collectors.toList());

        return new CustomUserDetails(
                foundUser.getId(),
                foundUser.getUsername(),
                foundUser.getPassword(),
                foundUser.getDeletedAt() == null,
                true,
                true,
                true,
                grantedAuthorities
        );
    }
}
