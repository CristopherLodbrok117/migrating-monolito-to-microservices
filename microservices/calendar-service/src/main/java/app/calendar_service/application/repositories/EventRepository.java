package app.calendar_service.application.repositories;

import app.calendar_service.domain.entities.Event;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("EventRepository")
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where id = :eventId")
    @EntityGraph(attributePaths = {"activities", "group", "category", "assignee", "files"})
    Optional<Event> findOneByIdWithDetails(@Param("eventId") Long id);
    List<Event> findAllByCreator_Id(Long creatorId);

    @EntityGraph(attributePaths = {"category"})
    List<Event> findAllByGroupId(Long groupId);
}
