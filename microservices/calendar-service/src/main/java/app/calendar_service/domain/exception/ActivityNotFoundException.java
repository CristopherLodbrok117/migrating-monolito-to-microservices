package app.calendar_service.domain.exception;

public class ActivityNotFoundException extends RuntimeException{
    public ActivityNotFoundException(Long id){
        super (String.format("Actividad con ID: %d no se encuentra registrada", id));
    }
}
