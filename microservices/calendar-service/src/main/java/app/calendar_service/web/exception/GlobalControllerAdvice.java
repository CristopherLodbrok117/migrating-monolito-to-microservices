package app.calendar_service.web.exception;

import app.calendar_service.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ActivityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<Map<String, String>> activityNotFoundHandler(ActivityNotFoundException ex){
        return retrieveErrorResponse(HttpStatus.NOT_FOUND,
                "activity error", ex.getMessage());
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus()
    ResponseEntity<Map<String,String>> eventNotFoundHandler(EventNotFoundException ex){
        return retrieveErrorResponse(HttpStatus.NOT_FOUND,
                "event error", ex.getMessage());
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    ResponseEntity<Map<String, String>> multipartMaxSizeExceptionHandler(MaxUploadSizeExceededException ex){
        return retrieveErrorResponse(HttpStatus.BAD_REQUEST,
                "Multipart file error", ex.getMessage());
    }

    @ExceptionHandler(UserException.class)
    ResponseEntity<Map<String, String>> userExceptionHandler(UserException ex){
        return retrieveErrorResponse(HttpStatus.BAD_REQUEST,
                "User error", ex.getMessage());
    }

    @ExceptionHandler(GroupException.class)
    ResponseEntity<Map<String, String>> groupExceptionHandler(GroupException ex){
        return retrieveErrorResponse(HttpStatus.BAD_REQUEST,
                "Group error", ex.getMessage());
    }

    @ExceptionHandler(EmailException.class)
    ResponseEntity<Map<String, String>> emailExceptionHandler(EmailException ex){
        return retrieveErrorResponse(HttpStatus.BAD_REQUEST,
                "Email error", ex.getMessage());
    }

    ResponseEntity<Map<String, String>> retrieveErrorResponse(HttpStatus statusCode,
                                                              String errorType, String message){
        return ResponseEntity.status(statusCode)
                .body(Map.of(errorType, message));
    }
}
