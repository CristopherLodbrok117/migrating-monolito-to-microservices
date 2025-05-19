package app.calendar_service.application.services;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.GroupRepository;
import app.calendar_service.application.repositories.MemberRoleRepository;
import app.calendar_service.application.repositories.SystemRoleRepository;
import app.calendar_service.application.usecases.UserService;
import app.calendar_service.domain.entities.MemberRole;
import app.calendar_service.domain.entities.Membership;
import app.calendar_service.domain.entities.SystemRole;
import app.calendar_service.domain.entities.User;
import app.calendar_service.application.repositories.UserRepository;
import app.calendar_service.web.dtos.UserDto;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final SystemRoleRepository systemRoleRepository;
    private final MemberRoleRepository memberRoleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findActiveUserById(Long id) {
        return userRepository.findOneByIdAndDeletedAtIsNull(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existByUsername(String username) {
        return userRepository.existsOneByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsOneByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAllByDeletedAtIsNull().stream()
                .map(UserDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findOne(Long id) {

        Optional<User> user = userRepository.findById(id);

        return user.map(UserDto::fromEntity)
                .or(Optional::empty);
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {

        User newUser = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .username(userDto.getUsername())
                .build()
                .withAuthority(systemRoleRepository.findOneByName("ROLE_USER").orElseThrow())
                .withMembershipToGroup(groupRepository.findById(1L).orElseThrow());

        User userDB = userRepository.save(newUser);

        Membership publicMembership = userDB.getMemberships().stream()
                .findFirst()
                .orElseThrow();

        publicMembership.setRoles(
                memberRoleRepository.findByNameIn(List.of("Descarga de Archivos", "Lectura de Eventos", "Editor General de Eventos"))
        );
        publicMembership.setAcceptedAt(LocalDateTime.now());

        userDB = userRepository.save(userDB);

        return UserDto.fromEntity(userDB);
    }

    @Override
    @Transactional
    public Optional<UserDto> update(UserDto userDto) {
        Optional<User> foundUser = userRepository.findById(userDto.getId());

        if(foundUser.isEmpty()){
            return Optional.empty();
        }

        User user = foundUser.orElseThrow();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());

        userRepository.save(user);

        return Optional.of(UserDto.fromEntity(user));
    }

    @Override
    @Transactional
    public Optional<UserDto> delete(Long id) {

        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty()){
            return  Optional.empty();
        }

        User deletingUser = user.orElseThrow();

        deletingUser.setDeletedAt(LocalDateTime.now());
        userRepository.save(deletingUser);

        return Optional.of(UserDto.fromEntity(deletingUser));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByUsernameWithAuth(String username){
        Optional<User> userOptional = userRepository.findOneByUsernameWithAuthorities(username);

        if(userOptional.isEmpty()){
            return Optional.empty();
        }

        User user = userOptional.orElseThrow();

        return Optional.of(
                UserDto.fromEntity(user)
                        .withAuthorities(user.getAuthorities())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findUserWithDetails(String username){
        Optional<User> userOptional = userRepository.findOneByUsernameWithDetails(username);

        if(userOptional.isEmpty()){
            return Optional.empty();
        }

        User user = userOptional.orElseThrow();

        return Optional.of(
                UserDto.fromEntity(user)
                        .withAuthorities(user.getAuthorities())
                        .withGroups(user.getGroups())
        );
    }
}
