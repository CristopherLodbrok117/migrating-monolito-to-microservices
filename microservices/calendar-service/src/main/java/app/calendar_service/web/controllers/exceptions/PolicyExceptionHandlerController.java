package app.calendar_service.web.controllers.exceptions;

import lombok.RequiredArgsConstructor;
import app.calendar_service.domain.classes.TimeZoneSolver;
import app.calendar_service.domain.exception.PolicyAccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class PolicyExceptionHandlerController {
    private final TimeZoneSolver timeZoneSolver;
    @ExceptionHandler({PolicyAccessDeniedException.class})
    public ResponseEntity<?> unsatisfiedPolicy(PolicyAccessDeniedException ex){
        Map<String, Object> responseBody = Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.FORBIDDEN.value(),
                "date", LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN.value())
                .body(responseBody);
    }
}
