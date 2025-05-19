package app.calendar_service.web.security.dao;

import lombok.Builder;

@Builder
public record SimpleAuthRecord(
        String username,
        String password,
        String email
) {
}
