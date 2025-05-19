package app.calendar_service.web.controllers;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.services.ActivityServiceImpl;
import app.calendar_service.application.usecases.ActivityService;
import app.calendar_service.web.dtos.ActivityDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
@PreAuthorize(value = "hasAnyRole('USER','SUPERUSER')")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityServiceImpl activityService;

    @GetMapping
    public ResponseEntity<List<ActivityDto>> findAllByEvent(@RequestParam("event") Long eventId){

        return ResponseEntity.ok(activityService.findAll(eventId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDto> findById(@PathVariable Long id){

        return ResponseEntity.ok(activityService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ActivityDto> create(@RequestBody ActivityDto activityDto,
                                              UriComponentsBuilder ucb){

        ActivityDto newActivity = activityService.create(activityDto);

        URI activityLocation = ucb
                .path("/api/activities/{id}")
                .buildAndExpand(newActivity.getId())
                .toUri();

        return ResponseEntity.created(activityLocation).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDto> update(@PathVariable Long id,
                                              @RequestBody ActivityDto activityDto){
        return ResponseEntity.ok(activityService.update(activityDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        activityService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/create-many-from-event/{eventId}")
    public ResponseEntity<?> createMany(@PathVariable(value = "eventId") Long eventId, @RequestBody List<ActivityDto> activities) {

        activityService.updateActivitiesFromEvent(eventId, activities);

        return ResponseEntity.ok().build();
    }
}
