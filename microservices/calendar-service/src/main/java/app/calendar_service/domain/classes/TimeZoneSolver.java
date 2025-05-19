package app.calendar_service.domain.classes;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.*;

@Component
@ApplicationScope
public class TimeZoneSolver {
    static private String TIME_ZONE = "America/Mexico_City";

    public LocalDateTime convertToLocal(LocalDateTime utcDateTime){
        ZonedDateTime zonedUTC = utcDateTime.atZone(ZoneOffset.UTC);
        ZonedDateTime mexicoDateTime = zonedUTC.withZoneSameInstant(ZoneId.of(TIME_ZONE));
        return  mexicoDateTime.toLocalDateTime();
    }
}
