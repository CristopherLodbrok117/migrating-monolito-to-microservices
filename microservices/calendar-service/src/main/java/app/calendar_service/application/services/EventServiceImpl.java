package app.calendar_service.application.services;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.ActivityRepository;
import app.calendar_service.application.repositories.GroupRepository;
import app.calendar_service.application.usecases.EventService;
import app.calendar_service.domain.classes.CategorySolver;
import app.calendar_service.domain.classes.TimeZoneSolver;
import app.calendar_service.domain.entities.*;
import app.calendar_service.domain.enums.EventPriority;
import app.calendar_service.domain.exception.EventNotFoundException;
import app.calendar_service.application.repositories.EventRepository;
import app.calendar_service.domain.policies.EventPolicy;
import app.calendar_service.web.dtos.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ActivityRepository activityRepository;
    private final GroupRepository groupRepository;

    private final EventPolicy policy;

    private final TimeZoneSolver timeZoneSolver;
    private final CategorySolver categorySolver;

    private final EntityManager entityManager;

    @Override
    public List<EventDto> findAllFromGroup(Long groupId) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto findById(Long groupId, Long id) {
        Group group = entityManager.getReference(Group.class, groupId);
        User authUser = policy.getAuthenticatedUser();

        policy.authorize("view", group);

        Event eventDB = eventRepository.findOneByIdWithDetails(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        return EventDto.fromEntity(eventDB)
                .withActivities(eventDB.getActivities())
                .withCreator(eventDB.getCreator())
                .withCategory(eventDB.getCategory())
                .withAssignee(eventDB.getAssignee())
                .withFiles(eventDB.getFiles())
                .withUserPermissions(
                        UserPermissionsDto.builder()
                                .deleteEvent(policy.delete(authUser, group, eventDB))
                                .updateEvent(policy.update(authUser, group, eventDB))
                                .build()
                );
    }

    @Override
    public List<EventDto> findByCreator(Long creatorId) {

        // actualizar a findAllByCreator_id
        return eventRepository.findAll().stream()
                .map(EventDto::fromEntity)
                .toList();
    }

    @Override
    public List<EventDto> findByGroup(Long groupId) {
        policy.authorize("view", entityManager.getReference(Group.class, groupId));

        return eventRepository.findAllByGroupId(groupId)
                .stream()
                .map(event -> EventDto.fromEntity(event)
                        .withCategory(event.getCategory()))
                .toList();
    }

    @Override
    @Transactional
    public EventDto create(Long groupId, EventDto eventDto) {
        Group group =  entityManager.getReference(Group.class, groupId);

        policy.authorize("create", group);

        User creator = entityManager.getReference(User.class, eventDto.getCreatorId());

        Event newEvent = Event.builder()
                .title(eventDto.getTitle())
                .notes((eventDto.getNotes()))
                .priority(eventDto.getPriority() != null ?  eventDto.getPriority() : EventPriority.Optional)
                .startDate(timeZoneSolver.convertToLocal(eventDto.getStartDate()))
                .endDate(timeZoneSolver.convertToLocal(eventDto.getEndDate()))
                .creator(creator)
                .group(group)
                .category(categorySolver.manageCategory(eventDto.getCategory(), groupId))
                .build();

        if(eventDto.getAssigneeId() != null && eventDto.getAssigneeId() > 0){
            User assignee = entityManager.getReference(User.class, eventDto.getAssigneeId());
            newEvent.setAssignee(assignee);
        }

        if (!eventDto.getFileIds().isEmpty()){
            Set<FileMetadata> files = eventDto.getFileIds().stream()
                    .map(fileId -> entityManager.getReference(FileMetadata.class, fileId))
                    .collect(Collectors.toSet());
            newEvent.setFiles(files);
        }

        return EventDto.fromEntity(eventRepository.save(newEvent));
    }

    @Override
    @Transactional
    public EventDto update(Long groupId, EventDto eventDto, Long id) {
        Event event = eventRepository.findById(id).orElseThrow();

        policy.authorize("update", entityManager.getReference(Group.class, groupId), event);

        if(!eventRepository.existsById(id)){
            return create(groupId, eventDto);
        }

        event.setTitle(eventDto.getTitle());
        event.setNotes((eventDto.getNotes()));
        event.setPriority(eventDto.getPriority() != null ?  eventDto.getPriority() : EventPriority.Optional);
        event.setStartDate(timeZoneSolver.convertToLocal(eventDto.getStartDate()));
        event.setCategory(categorySolver.manageCategory(eventDto.getCategory(), groupId));
        event.setEndDate(timeZoneSolver.convertToLocal(eventDto.getEndDate()));

        User assignee = null;
        if (eventDto.getAssigneeId() != null && eventDto.getAssigneeId() > 0){
            assignee = entityManager.getReference(User.class, eventDto.getAssigneeId());
        }
        event.setAssignee(assignee);

        Set<FileMetadata> files = eventDto.getFileIds().stream()
                .map(fileId -> entityManager.getReference(FileMetadata.class, fileId))
                .collect(Collectors.toSet());
        event.setFiles(files);

        return EventDto.fromEntity(eventRepository.save(event));
    }

    @Override
    @Transactional
    public void delete(Long groupId, Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if(event.isEmpty()){
            throw new EventNotFoundException(id);
        }

        policy.authorize("delete", entityManager.getReference(Group.class, groupId), event.get());

        activityRepository.deleteAllByEventId(event.get().getId());
        eventRepository.deleteById(id);
    }
    
    private  Map<String, Object> fetchFormResources(Long groupId){
        Group group = groupRepository.fetchEventFormResourcesByGroup(groupId).orElseThrow();

        List<UserDto> members = group.getMembers().stream().map(UserDto::fromEntity).toList();
        List<FileMetadataDto> files = group.getFiles().stream().map(FileMetadataDto::fromEntity).toList();
        List<CategoryDto> categories = group.getCategories().stream().map(CategoryDto::fromEntity).toList();

        return Map.of(
                "members", members,
                "files", files,
                "categories", categories
        );
    }

    @Override
    public Map<String, Object> prepareCreateForm(Long groupId){
        policy.authorize("create", entityManager.getReference(Group.class, groupId));

        Map<String,Object> resources = fetchFormResources(groupId);

        return Map.of("resources", resources);
    }

    @Override
    public Map<String, Object> prepareEditForm(Long groupId, Long eventId){
        policy.authorize("update", entityManager.getReference(Group.class, groupId));

        Map<String,Object> resources = fetchFormResources(groupId);
        Event event = eventRepository.findOneByIdWithDetails(eventId).orElseThrow();

        EventDto eventDto = EventDto.fromEntity(event);
        eventDto.setCategoryId(event.getCategoryId());
        eventDto.setCreatorId(event.getCreatorId());
        eventDto.setAssigneeId(event.getAssigneeId());

        return Map.of(
                "event", event,
                "resources", resources
        );
    }
}
