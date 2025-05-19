package app.calendar_service.application.usecases;

import app.calendar_service.web.dtos.EventDto;
import app.calendar_service.web.dtos.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface EventService {

    EventDto findById(Long groupId, Long id);

    List<EventDto> findAllFromGroup(Long groupId);

    List<EventDto> findByCreator(Long userId);

    List<EventDto> findByGroup(Long groupId);

    EventDto create(Long groupId, EventDto eventDto);

    EventDto update(Long groupId, EventDto eventDto, Long id);

    void delete(Long groupId, Long id);

    public Map<String, Object> prepareEditForm(Long groupId, Long eventId);
    public Map<String, Object> prepareCreateForm(Long groupId);
}
