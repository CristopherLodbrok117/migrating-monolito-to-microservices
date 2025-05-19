package app.calendar_service.domain.policies;

import app.calendar_service.application.repositories.UserRepository;
import app.calendar_service.domain.entities.MemberRole;
import app.calendar_service.domain.entities.User;
import app.calendar_service.domain.exception.PolicyAccessDeniedException;
import app.calendar_service.domain.exception.PolicyNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SinaloaPolicy {
    protected final UserRepository userRepository;
    private final Map<String,Method> methods;

    protected SinaloaPolicy(UserRepository userRepository) {
        this.userRepository = userRepository;
        Stream<Method> methodStream = Stream.of(this.getClass().getMethods());

        this.methods = methodStream.filter(method -> method.toString().startsWith("public boolean " + this.getClass().getPackage().getName()))
                .collect(Collectors.toMap(Method::getName, Function.identity()));
    }

    protected boolean hasAnyRole(Set<String> validRoles, Set<MemberRole> roles) {
        return roles.stream()
                .anyMatch(memberRole -> validRoles.contains(memberRole.getName()));
    }

    public User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        return userRepository.findOneByUsername(username).orElseThrow(()-> new AuthenticationException("User not found") {});
    }

    /**
     * Maneja la policy
     *
     * @param policy nombre del metodo de la policy a validar
     * @param args argumentos de esa policy, sin contar el usuario
     */
    public void authorize(String policy, Object ...args){
        boolean success;

        try {
            Method method = methods.get(policy);

            Object[] argsWithUser = Stream.concat(Stream.of(getAuthenticatedUser()), Stream.of(args))
                    .toArray();

            method.setAccessible(true);
            success = (Boolean) method.invoke(this, argsWithUser);

        } catch (NullPointerException | InvocationTargetException | IllegalAccessException e) {
            throw new PolicyNotFoundException();
        }

        if (!success) {
            throw new PolicyAccessDeniedException("La petición fue rechazada por no tener autorización para la acción "
                    + policy + " sobre el recurso solicitado.");
        }
    }
}
