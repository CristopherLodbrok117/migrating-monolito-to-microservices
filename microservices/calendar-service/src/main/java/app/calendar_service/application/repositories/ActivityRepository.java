package app.calendar_service.application.repositories;

import app.calendar_service.domain.entities.Activity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("ActivityRepository")
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByEvent_Id(Long eventId);

    void deleteAllByEventId(Long creatorId);
}
