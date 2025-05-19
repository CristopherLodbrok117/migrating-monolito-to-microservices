package app.calendar_service.application.usecases;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.ActivityRepository;
import app.calendar_service.web.dtos.ActivityDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ActivityService {

    public ActivityDto findById(long id);

    public List<ActivityDto> findAll(Long eventId);

    public ActivityDto create(ActivityDto activityDto);

    public ActivityDto update(ActivityDto activityDto, Long id);

    public void delete(Long id);

    void updateActivitiesFromEvent(Long eventId, List<ActivityDto> activities);
}
