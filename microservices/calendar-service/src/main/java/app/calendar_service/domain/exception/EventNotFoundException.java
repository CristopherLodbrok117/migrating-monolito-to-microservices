package app.calendar_service.domain.exception;

public class EventNotFoundException extends RuntimeException{

    public EventNotFoundException(Long id){
        super(String.format("Evento con ID: %d no esta registrado", id));
    }
}
