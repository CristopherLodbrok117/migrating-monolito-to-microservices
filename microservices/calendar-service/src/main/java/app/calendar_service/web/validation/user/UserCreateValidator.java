package app.calendar_service.web.validation.user;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.usecases.UserService;
import app.calendar_service.domain.entities.User;
import app.calendar_service.web.dtos.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserCreateValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto user = (UserDto) target;

        if(userService.existByUsername(user.getUsername())){
            errors.rejectValue("username", "" ,"ya ha sido registrado");
        }

        if(userService.existsByEmail(user.getEmail())){
            errors.rejectValue("email", "" ,"ya ha sido registrado");
        }
    }
}
