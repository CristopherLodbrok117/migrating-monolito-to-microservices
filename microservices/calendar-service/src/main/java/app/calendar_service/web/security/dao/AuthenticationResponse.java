package app.calendar_service.web.security.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.lang.Nullable;

@Builder
public record AuthenticationResponse(
        @Nullable
        @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
        String token,

        @Nullable
        @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
        String username,

        @Nullable
        @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
        String error,

        @Nullable
        @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
        String message,

        @Nullable
        @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
        Long uid
) {
}
