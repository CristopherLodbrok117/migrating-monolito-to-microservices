package app.calendar_service.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import app.calendar_service.application.usecases.UserService;
import app.calendar_service.web.dtos.UserDto;
import app.calendar_service.web.validation.user.UserCreateValidator;
import app.calendar_service.web.validation.user.UserUpdateValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserUpdateValidator updateValidator;

    private final UserCreateValidator createValidator;

    private ResponseEntity<?> responseWithErrors(BindingResult bindingResult){
        Map<String,String> errors =  new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }


    @GetMapping("/")
    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok().body(
                userService.findAll()
        );
    }
    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<?> show(@PathVariable(name = "id") Long id){

        Optional<UserDto> user = userService.findOne(id);

        if(user.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(
                user.orElseThrow()
        );
    }

    @PostMapping("/")
    @PreAuthorize(value = "hasAnyRole('USER','ADMIN','SUPERUSER')")
    public ResponseEntity<?> create(@Valid @RequestBody UserDto userDto, BindingResult bindingResult){

        if(bindingResult.hasFieldErrors()){
            return responseWithErrors(bindingResult);
        }

        createValidator.validate(userDto, bindingResult);
        if(bindingResult.hasFieldErrors()){
            return responseWithErrors(bindingResult);
        }

        UserDto user =  userService.create(userDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<?> update(@Valid @RequestBody UserDto userDto, BindingResult bindingResult, @PathVariable(name = "id") Long id){

        if(bindingResult.hasFieldErrors()){
            return responseWithErrors(bindingResult);
        }

        userDto.setId(id);
        updateValidator.validate(userDto, bindingResult);
        if(bindingResult.hasFieldErrors()){
            return responseWithErrors(bindingResult);
        }

        Optional<UserDto> userDtoOptional = userService.update(userDto);

        if(userDtoOptional.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(
                userDtoOptional.orElseThrow()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id){

        Optional<UserDto> user = userService.delete(id);

        if(user.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(
                user.orElseThrow()
        );
    }

    @GetMapping("/logged")
    public ResponseEntity<?> logged(@AuthenticationPrincipal String username){
        Optional<UserDto> userDto = userService.findUserWithDetails(username);

        if(userDto.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .body(userDto.orElseThrow());
    }
}
