package app.calendar_service.application.services;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.usecases.ActivityService;
import app.calendar_service.domain.exception.ActivityNotFoundException;
import app.calendar_service.domain.exception.EventNotFoundException;
import app.calendar_service.domain.entities.Activity;
import app.calendar_service.domain.entities.Event;
import app.calendar_service.application.repositories.ActivityRepository;
import app.calendar_service.application.repositories.EventRepository;
import app.calendar_service.web.dtos.ActivityDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final EventRepository eventRepository;

    @Override
    public ActivityDto findById(long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException(id));

        return ActivityDto.fromEntity(activity);
    }

    @Override
    public List<ActivityDto> findAll(Long eventId) {

        return activityRepository.findAllByEvent_Id(eventId)
                .stream()
                .map(ActivityDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public ActivityDto create(ActivityDto activityDto) {
        Event event = eventRepository.findById(activityDto.getEventId())
                .orElseThrow(() -> new EventNotFoundException(activityDto.getEventId()));

        Activity activity = Activity.builder()
                .name(activityDto.getName())
                .status(activityDto.getStatus())
                .event(event)
                .build();

        return ActivityDto.fromEntity(activityRepository.save(activity));
    }


    @Override
    @Transactional
    public ActivityDto update(ActivityDto activityDto, Long id) {
        if(!activityRepository.existsById(id)){
            throw new ActivityNotFoundException(id);
        }

        Event event = eventRepository.findById(activityDto.getEventId())
                .orElseThrow(() -> new EventNotFoundException(activityDto.getEventId()));

        Activity newActivity = Activity.builder()
                .id(id)
                .name(activityDto.getName())
                .status(activityDto.getStatus())
                .event(event)
                .build();

        Activity activityDB = activityRepository.save(newActivity);

        return ActivityDto.fromEntity(activityDB);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if(!activityRepository.existsById(id)){
            throw new ActivityNotFoundException(id);
        }

        activityRepository.deleteById(id);
    }


    /**
     *  Al chile, este metodo elimina las actividades registradas en el evento y
     *  sobrescribe las actividades de la coleecion entrante
     *
     * @param eventId
     * @param activities
     */
    @Transactional
    @Override
    public void updateActivitiesFromEvent(Long eventId, List<ActivityDto> activities){

        final Event event = eventRepository.findById(eventId).orElseThrow();

        activityRepository.deleteAllByEventId(eventId);

        if (activities.isEmpty()){
            return;
        }

        activityRepository.saveAll(
                activities.stream().map(activityDto -> Activity.builder()
                        .name(activityDto.getName())
                        .status(activityDto.getStatus())
                        .event(event)
                        .build()
                ).toList()
        );
    }
}
