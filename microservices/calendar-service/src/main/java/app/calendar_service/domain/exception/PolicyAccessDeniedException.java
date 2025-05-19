package app.calendar_service.domain.exception;

import org.springframework.security.access.AccessDeniedException;

public class PolicyAccessDeniedException extends AccessDeniedException {
    public PolicyAccessDeniedException(String msg) {
        super(msg);
    }
}
