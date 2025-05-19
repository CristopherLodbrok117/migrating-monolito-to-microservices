package app.calendar_service.web.controllers;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.services.EventServiceImpl;
import app.calendar_service.web.dtos.EventDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups/{groupId}/events")
@RequiredArgsConstructor
@PreAuthorize(value = "hasAnyRole('USER','SUPERUSER', 'ADMIN')")
@CrossOrigin(("http://localhost:5173"))
public class EventController {

    private final EventServiceImpl eventService;

    @GetMapping
    public ResponseEntity<List<EventDto>> findAll(@PathVariable Long groupId){
        return ResponseEntity.ok(eventService.findByGroup(groupId));
    }

    @GetMapping("/{id}")
    //@PreAuthorize(value = "hasAnyRole('USER','SUPERUSER')")
    public ResponseEntity<EventDto> findById(@PathVariable Long groupId, @PathVariable Long id){
        return ResponseEntity.ok(eventService.findById(groupId, id));
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable Long groupId, @RequestBody EventDto eventDto
            , UriComponentsBuilder ucb){

        EventDto newEvent = eventService.create(groupId, eventDto);

        URI eventUri = ucb
                .path("/api/groups/{groupId}/events/{id}")
                .buildAndExpand(groupId,newEvent.getId())
                .toUri();

        return ResponseEntity.created(eventUri).body(newEvent);

    }

    @PutMapping("/{id}")
    //@PreAuthorize(value = "hasAnyRole('USER','SUPERUSER')")
    public ResponseEntity<EventDto> update(@PathVariable Long groupId, @PathVariable Long id, @RequestBody EventDto eventDto){
        return ResponseEntity.ok(eventService.update(groupId, eventDto, id));
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize(value = "hasAnyRole('USER','SUPERUSER')")
    public ResponseEntity<?> delete(@PathVariable Long groupId, @PathVariable Long id){
        eventService.delete(groupId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/create")
    public ResponseEntity<?> openCreateForm(@PathVariable Long groupId){
        Map<String, Object> formResources = eventService.prepareCreateForm(groupId);
        return ResponseEntity.ok().body(formResources);
    }

    @GetMapping("{id}/edit")
    public ResponseEntity<?> openEditForm(@PathVariable Long groupId, @PathVariable Long eventId){
        Map<String, Object> formResources = eventService.prepareEditForm(groupId, eventId);
        return ResponseEntity.ok().body(formResources);
    }
}
