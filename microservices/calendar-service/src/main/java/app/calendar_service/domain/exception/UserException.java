package app.calendar_service.domain.exception;

public class UserException extends RuntimeException{
    public UserException(String msg){
        super(msg);
    }
}
