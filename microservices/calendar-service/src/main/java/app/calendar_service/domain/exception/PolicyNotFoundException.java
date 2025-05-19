package app.calendar_service.domain.exception;

public class PolicyNotFoundException extends RuntimeException {
    public PolicyNotFoundException() {
        super("No se encontró la policy ingresada");
    }
}
