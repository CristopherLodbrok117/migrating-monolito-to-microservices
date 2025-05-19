package app.calendar_service.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import app.calendar_service.application.security.services.JwtService;
import app.calendar_service.application.usecases.UserService;
import app.calendar_service.web.dtos.UserDto;
import app.calendar_service.web.security.config.JwtEnv;
import app.calendar_service.web.security.dao.AuthenticationResponse;
import app.calendar_service.web.validation.user.UserCreateValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserCreateValidator createValidator;
    private final JwtService jwtService;

    private ResponseEntity<?> responseWithErrors(BindingResult bindingResult){
        Map<String,String> errors =  new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody UserDto userDto, BindingResult bindingResult){

        if(bindingResult.hasFieldErrors()){
            return responseWithErrors(bindingResult);
        }

        createValidator.validate(userDto, bindingResult);
        if(bindingResult.hasFieldErrors()){
            return responseWithErrors(bindingResult);
        }

        UserDto user =  userService.create(userDto);

        UserDto userWithAuth = userService.findByUsernameWithAuth(user.getUsername()).orElseThrow();

        return generateJwt(userWithAuth);
    }

    @GetMapping("/renew")
    public ResponseEntity<?> renew(@AuthenticationPrincipal String username) {
        UserDto userDto = userService.findByUsernameWithAuth(username).orElseThrow();

        return this.generateJwt(userDto);
    }

    private ResponseEntity<?>  generateJwt(UserDto userDto){
        try {
            List<GrantedAuthority> grantedAuthorities = userDto.getAuthorities().stream()
                    .map((auth) -> new SimpleGrantedAuthority(auth.getName()))
                    .collect(Collectors.toList());

            String token = jwtService.generateToken(
                    new User(
                            userDto.getUsername(),
                            userDto.getPassword(),
                            true,
                            true,
                            true,
                            true,
                            grantedAuthorities
                    )
            );

            return ResponseEntity.ok(
                    AuthenticationResponse.builder()
                            .token(JwtEnv.PREFIX_TOKEN + token)
                            .username(userDto.getUsername())
                            .message("Token Refrescado")
                            .uid(userDto.getId())
                            .build()
            );

        } catch (JsonProcessingException jsonProcessingException){
            return ResponseEntity.notFound().build();
        }
    }
}
