package app.calendar_service.application.usecases;

import app.calendar_service.domain.entities.User;
import app.calendar_service.web.dtos.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> findAll();
    Optional<UserDto> findOne(Long id);
    UserDto create(UserDto userDto);
    Optional<UserDto> update(UserDto userDto);
    Optional<UserDto> delete(Long id);
    Optional<User> findActiveUserById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existByUsername(String username);
    boolean existsByEmail(String email);
    Optional<UserDto> findUserWithDetails(String username);
    Optional<UserDto> findByUsernameWithAuth(String username);
}
