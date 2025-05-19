package app.calendar_service.application.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.*;
import app.calendar_service.application.usecases.MailService;
import app.calendar_service.domain.entities.*;
import app.calendar_service.domain.exception.EmailException;
import app.calendar_service.web.seeders.DatabaseSeeder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class MailServiceImpl implements MailService {

    private static final long TIME_INTERVAL_MILLIS = 600_000; // Cada dos horas

    @Value("${sinaloa.backend.urls.groups}")
    private String GROUP_URL;

    @Value("${spring.mail.password}") // Define esto en tu application.properties
    private String sendGridApiKey;


    private String sinaloaEmail  = "cristopheralexis.chavez@alumnos.udg.mx";

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final InvitationTokenRepository tokenRepository;

    private final DatabaseSeeder databaseSeeder;


    @Override
    @Scheduled(fixedDelay = TIME_INTERVAL_MILLIS)
    public void monitoringDailyEvents(){
        System.out.println("Monitoreando eventos cada " + (TIME_INTERVAL_MILLIS)/1000 + " segundos");

        LocalTime currentTime = LocalTime.now();
        LocalTime start = LocalTime.of(11, 0);
        LocalTime end = LocalTime.of(12, 0);

        if(isTimeWithinRange(currentTime, start, end)){
            System.out.println("+Tiempo en rango. Enviando mensajes con eventos del dia");

//            sendEventNotifications();
        }
        else{
            System.out.println("-No es hora de enviar mensajes aun...");
        }

    }

    @Override
    public void sendEventNotifications(){
        // Traer eventos del dia
        Map<Long, Event> groupEvents = getTodayEventsByGroup();
        // Crear map
        Map<User, List<Event>> userWithEvents = getUserWithEvents(groupEvents);
        Map<User, String> userWithStrEvents = new HashMap<>();

        // Pasar lista a string con <br> de separador
        for( Map.Entry<User, List<Event>> entry : userWithEvents.entrySet()){
            String eventsStr = entry.getValue().stream()
                    .map(Event::getTitle)
                    .map(eventStr -> "<li class='item'>" + eventStr + "</li>")
                    .collect(Collectors.joining(""));

            userWithStrEvents.put(entry.getKey(), eventsStr);
        }

        // Envia rmensaje a cada usuario con su lista de eventos
        userWithStrEvents.forEach((user, eventsStr) -> sendEventsNotification(
                user.getEmail(),
                "Eventos del dia - SinaloaApp",
                user.getUsername(),
                eventsStr));

    }

    /* Obetner  eventos del dia, mapeados por el código de su grupo*/
    public Map<Long, Event> getTodayEventsByGroup(){

        List<Event> events = eventRepository.findAll();

        LocalDate today = LocalDate.now();

        events = events.stream()
                .filter(e -> e.getStartDate().toLocalDate().isEqual(today))
                .toList();

        return events.stream()
                .collect(Collectors.toMap(event -> event.getGroup().getId(),
                        event -> event));
    }

    /* Obtenemos un map de usuarios y su lista de eventos (del dia actual) */
    public Map<User, List<Event>> getUserWithEvents(Map<Long, Event> groupEvents){
        // Obtener lista de usuarios
        List<User> users = userRepository.findAllUsersWithMembershipsAndGroups();

        // Crear map para usuarios con sus eventos
        Map<User, List<Event>> userWithEvents = new HashMap<>();

        for(User user: users){
            for(Membership membership : user.getMemberships()){
                long groupId = membership.getGroup().getId();

                if(groupEvents.containsKey(groupId)){
                    Event newEvent = groupEvents.get(groupId);

                    if(userWithEvents.containsKey(user)){
                        userWithEvents.get(user).add(newEvent);
                    }
                    else{
                        List<Event> userEvents = new ArrayList<>();
                        userEvents.add(newEvent);

                        userWithEvents.put(user, userEvents);
                    }

                }
            }
        }

        /* Solo devolvera usuarios con eventos */
        return userWithEvents;
    }

    @Override
    public void sendGroupInvitation( User user, Group group, Long membershipId, InvitationToken token){

        String invitationUrl = GROUP_URL + group.getId()
                + "/accept-invitation?membershipId=" + membershipId
                + "&invitationToken=" + token.getToken();

        sendHtmlEmail(user.getEmail(),
                "Invitación a grupo - SinaloaApp",
                user.getUsername(),
                group.getName(),
                invitationUrl);
    }

    /* Envio de mensajes sin templates */
    @Override
    public void sendEmail(String dest, String subject, String body) {


    }

    /* Envio de mensajes con templates*/


    public void sendEventsNotification(String to,
                              String subject,
                              String username,
                              String events) {



    }

    private String loadHtmlTemplate(String path) {
        String html;

        try{
            ClassPathResource resource = new ClassPathResource(path);
            html = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        } catch(IOException ex){
            throw new EmailException(ex.getMessage());
        }

        return html;
    }

    private static boolean isTimeWithinRange(LocalTime timeToCheck, LocalTime startTime, LocalTime endTime ){
        return !timeToCheck.isBefore(startTime) && timeToCheck.isBefore(endTime);
    }

    public void seedDatabase(){

        databaseSeeder.seedAll();
    }

    public void sendHtmlEmail(String to,
                             String subject,
                             String username,
                             String groupName,
                             String invitationUrl){
        try {
            // 1. Cargar plantilla HTML
            String html = loadHtmlTemplate("template/email/invitation.html");

            // 2. Reemplazar variables
            html = html.replace("{{username}}", username)
                    .replace("{{groupName}}", groupName)
                    .replace("{{invitationUrl}}", invitationUrl);

            // 3. Configurar el correo con SendGrid
            Email from = new Email(sinaloaEmail);
            Email toEmail = new Email(to);
            Content content = new Content("text/html", html);

            Mail mail = new Mail(from, subject, toEmail, content);

            // 4. Enviar con la API de SendGrid
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            // 5. Manejar errores
            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                throw new EmailException("Error al enviar: " + response.getStatusCode() + " - " + response.getBody());
            }

        } catch (IOException ex) {
            throw new EmailException("Error al cargar la plantilla: " + ex.getMessage());
        } catch (Exception ex) {
            throw new EmailException("Error inesperado: " + ex.getMessage());
        }
    }
}
