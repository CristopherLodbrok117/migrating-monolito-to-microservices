package app.calendar_service.domain.exception;

public class EmailException extends RuntimeException{
    public EmailException(String msg){
        super(msg);
    }
}
