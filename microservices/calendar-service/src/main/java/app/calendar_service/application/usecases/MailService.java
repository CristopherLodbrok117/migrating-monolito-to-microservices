package app.calendar_service.application.usecases;

import app.calendar_service.domain.entities.Event;
import app.calendar_service.domain.entities.Group;
import app.calendar_service.domain.entities.InvitationToken;
import app.calendar_service.domain.entities.User;


import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface MailService {
    public void monitoringDailyEvents();

    public void sendGroupInvitation(User user, Group group, Long membershipId, InvitationToken token);

    public void sendEmail(String dest, String subject, String body);
    public void sendHtmlEmail(String to,String subject, String username,
                              String groupName, String invitationUrl);
    public void sendEventNotifications();

    public Map<Long, Event> getTodayEventsByGroup();
    public Map<User, List<Event>> getUserWithEvents(Map<Long, Event> groupEvents);
    public void sendEventsNotification(String to, String subject,
                                       String username, String events);

}
