package app.calendar_service.web.seeders;

import lombok.RequiredArgsConstructor;
import app.calendar_service.application.repositories.*;
import app.calendar_service.domain.entities.*;
import app.calendar_service.domain.enums.ActivityStatus;
import app.calendar_service.domain.enums.EventPriority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UseCaseSeeder implements Seeder {

    private final UserRepository userRepository;
    private final SystemRoleRepository systemRoleRepository;
    private final GroupRepository groupRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ActivityRepository activityRepository;

    private final PasswordEncoder passwordEncoder;

    private long groupId;

    @Transactional
    private void createUsers(){
        Set<SystemRole> systemRoles =  new HashSet<>();
        SystemRole userRole = systemRoleRepository.findOneByName("ROLE_USER").orElseThrow();
        systemRoles.add(userRole);

        List<User> userList = new ArrayList<>();

        userList.add(
                User.builder()
                        .email("romero22@gestorsinaloa.com")
                        .username("josueromero1")
                        .password(passwordEncoder.encode("secreto123"))
                        .authorities(systemRoles)
                        .build()
        );

        userList.add(
                User.builder()
                        .email("lauradev@gestorsinaloa.com")
                        .username("lauraS2002")
                        .password(passwordEncoder.encode("secreto123"))
                        .authorities(systemRoles)
                        .build()
        );

        userList.add(
                User.builder()
                        .email("andrea99@gestorsinaloa.com")
                        .username("andreasanchez99")
                        .password(passwordEncoder.encode("secreto123"))
                        .authorities(systemRoles)
                        .build()
        );

        userList.add(
                User.builder()
                        .email("elbicho7siuuu@gestorsinaloa.com")
                        .username("santisoto7")
                        .password(passwordEncoder.encode("secreto123"))
                        .authorities(systemRoles)
                        .build()
        );

        userRepository.saveAll(userList);
    }

    @Transactional
    private void createGroupAndMemberships(){
        Group group = groupRepository.save(
                Group.builder()
                        .name("Monster Mansion: Proyecto Web")
                        .creator(userRepository.findOneByUsername("lauraS2002").orElseThrow())
                        .build()
        );

        groupId = group.getId();

        Set<Membership> memberships = new HashSet<>();

        memberships.add(
                Membership.builder()
                        .group(group)
                        .user(userRepository.findOneByUsername("lauraS2002").orElseThrow())
                        .roles(memberRoleRepository.findByNameIn("Creador", "Descarga de Archivos", "Lectura de Eventos"))
                        .acceptedAt(LocalDateTime.now())
                        .build()
        );

        Set<MemberRole> memberRoles = memberRoleRepository.findByNameIn("Descarga de Archivos", "Lectura de Eventos");

        memberships.add(
                Membership.builder()
                        .group(group)
                        .user(userRepository.findOneByUsername("josueromero1").orElseThrow())
                        .roles(memberRoles)
                        .acceptedAt(LocalDateTime.now())
                        .build()
        );

        memberships.add(
                Membership.builder()
                        .group(group)
                        .user(userRepository.findOneByUsername("andreasanchez99").orElseThrow())
                        .roles(memberRoles)
                        .acceptedAt(LocalDateTime.now())
                        .build()
        );

        memberships.add(
                Membership.builder()
                        .group(group)
                        .user(userRepository.findOneByUsername("santisoto7").orElseThrow())
                        .roles(memberRoles)
                        .acceptedAt(LocalDateTime.now())
                        .build()
        );

        group.setMemberships(memberships);
        groupRepository.save(group);
    }

    @Transactional
    private void createCategories() {
        Group group = groupRepository.findById(groupId).orElseThrow();

        List<Category> categories = new ArrayList<>();
        categories.add(  Category.builder()
                        .name("Desarrollo Front-End")
                        .group(group)
                        .build()
        );
        categories.add(
                Category.builder()
                        .name("Desarrollo Back-End")
                        .group(group)
                        .build()
        );
        categories.add(
                Category.builder()
                        .name("Diseño")
                        .group(group)
                        .build()
        );
        categories.add(
                Category.builder()
                        .name("Pruebas y Depuración")
                        .group(group)
                        .build()
        );
        categories.add(
                Category.builder()
                        .name("Despliegue")
                        .group(group)
                        .build()
        );
        categories.add(
                Category.builder()
                        .name("Documentación")
                        .group(group)
                        .build()
        );
        categoryRepository.saveAll(categories);
    }

    @Transactional
    private void createEventsAndActivities() {
        Group group = groupRepository.findByIdUseCase(groupId).orElseThrow();
        Map<String,User> members = group.getMemberships().stream()
                .collect(
                        Collectors.toMap(
                                membership -> membership.getUser().getUsername(),
                                Membership::getUser
                        )
                );

        Map<String,Category> categories = group.getCategories().stream()
                .collect(
                        Collectors.toMap(
                                Category::getName,
                                Function.identity()
                        )
                );

        List<Event> events = new ArrayList<>();

        LocalDateTime dateTime = LocalDateTime.now().withHour(10).withMinute(0);
        Event event = Event.builder()
                .title("Diseño de Base de Datos")
                .category(categories.get("Diseño"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("andreasanchez99"))
                .activities(
                        buildActivities("Diagrama Entidad, Relación", "Diccionario de Datos")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusHours(6));
        events.add(event);

        event = Event.builder()
                .title("Diseño de arquitectura de aplicación")
                .category(categories.get("Diseño"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("andreasanchez99"))
                .activities(
                        buildActivities("Diagrama de componentes", "Diagrama de clases")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusHours(6));
        events.add(event);

        event = Event.builder()
                .title("Diseño de UX/UI")
                .category(categories.get("Diseño"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("josueromero1"))
                .activities(
                        buildActivities("Mockup de vista de inicio", "Mockup de vista de reservaciones", "Mockup de vista de contacto")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusHours(6));
        events.add(event);

        dateTime = dateTime.plusDays(1);

        event = Event.builder()
                .title("Diseño de casos de uso")
                .category(categories.get("Diseño"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("lauraS2002"))
                .activities(
                        buildActivities("Listar casos de uso", "Diagrama de casos de uso")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusHours(6));
        events.add(event);

        dateTime = dateTime.plusDays(1);

        event = Event.builder()
                .title("Diseño de flujo de aplicación")
                .category(categories.get("Diseño"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("andreasanchez99"))
                .activities(
                        buildActivities("Diagrama secuencial: Login", "Diagrama secuencial: Reservación", "Diagrama secuencial: Contacto")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusHours(6));
        events.add(event);

        dateTime = dateTime.plusDays(1);

        event = Event.builder()
                .title("Desarrollo Front-End: Modulo de Login")
                .category(categories.get("Desarrollo Front-End"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("josueromero1"))
                .activities(
                        buildActivities("Incio de sesión", "Registrar usuario", "Pantalla de sesion expirada")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusDays(5));
        events.add(event);

        event = Event.builder()
                .title("Desarrollo Back-End: Modulo de Login")
                .category(categories.get("Desarrollo Back-End"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("andreasanchez99"))
                .activities(
                        buildActivities("Incio de sesión", "Registrar usuario", "Manejo de sesiones")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusDays(5));
        events.add(event);

        dateTime = dateTime.plusDays(6);

        event = Event.builder()
                .title("Desarrollo Front-End: Modulo de Reservaciones")
                .category(categories.get("Desarrollo Front-End"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("josueromero1"))
                .activities(
                        buildActivities("Vista de precios", "Formulario de transacción", "Vista de error")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusDays(10));
        events.add(event);

        event = Event.builder()
                .title("Desarrollo Back-End: Modulo de Reservaciones")
                .category(categories.get("Desarrollo Back-End"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("andreasanchez99"))
                .activities(
                        buildActivities("Pasarela de pago", "Transaccion en base de datos", "Sincronizacion con sistema de contabilidad")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusDays(10));
        events.add(event);

        dateTime = dateTime.plusDays(11);

        event = Event.builder()
                .title("Desarrollo Front-End: Modulo de Contacto")
                .category(categories.get("Desarrollo Front-End"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("josueromero1"))
                .activities(
                        buildActivities("Pantalla de inicio", "Vista de contactos")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusDays(5));
        events.add(event);

        event = Event.builder()
                .title("Desarrollo Back-End: Modulo de Contacto")
                .category(categories.get("Desarrollo Back-End"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("andreasanchez99"))
                .activities(
                        buildActivities("Sistema de mensajes de contacto", "Sincronización con soporte")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusDays(5));
        events.add(event);

        dateTime = dateTime.plusDays(6);

        event = Event.builder()
                .title("Fase de pruebas Unitarias")
                .category(categories.get("Pruebas y Depuración"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("santisoto7"))
                .activities(
                        buildActivities("Pruebas: login", "Pruebas: contacto", "Pruebas: reservaciones")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusDays(5));
        events.add(event);

        dateTime = dateTime.plusDays(6);

        event = Event.builder()
                .title("Depuración general")
                .category(categories.get("Pruebas y Depuración"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("josueromero1"))
                .activities(
                        buildActivities("Depuración: login", "Depuración: contacto", "Depuración: reservaciones")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusDays(5));
        events.add(event);

        dateTime = dateTime.plusDays(6);

        event = Event.builder()
                .title("Validación final")
                .category(categories.get("Pruebas y Depuración"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("santisoto7"))
                .activities(
                        buildActivities("Pruebas: login", "Pruebas: contacto", "Pruebas: reservaciones")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusDays(2));
        events.add(event);

        dateTime = dateTime.plusDays(3);

        event = Event.builder()
                .title("Despliegue en AWS")
                .category(categories.get("Despliegue"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .activities(
                        buildActivities("Configuración EC2", "Configuración RDS MySql", "Configuración de DNS")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusDays(7));
        events.add(event);

        dateTime = dateTime.plusDays(8);

        event = Event.builder()
                .title("Cierre de Proyecto")
                .category(categories.get("Despliegue"))
                .priority(EventPriority.Medium)
                .group(group)
                .notes("")
                .creator(members.get("lauraS2002"))
                .assignee(members.get("lauraS2002"))
                .activities(
                        buildActivities("Validación Final")
                )
                .build();
        event.setStartDate(dateTime);
        event.setEndDate(dateTime.plusDays(2));
        events.add(event);

        eventRepository.saveAll(events);

        List<Activity> activityList = events.stream().map( event1 -> {
                    event1.getActivities().forEach(activity -> activity.setEvent(event1));
                    return event1.getActivities();
        })
                .flatMap(Collection::stream)
                .toList();

        activityRepository.saveAll(activityList);
    }

    private List<Activity> buildActivities(String... names){
        List<Activity> activities = new ArrayList<>();
        Arrays.stream(names).forEach( name ->{
            activities.add(
                    Activity.builder()
                            .name(name)
                            .status(ActivityStatus.NO_INICIADA)
                            .build()
            );
        });
        return activities;
    }

    @Override
    public void seed() {
        createUsers();
        createGroupAndMemberships();
        createCategories();
        createEventsAndActivities();
    }

    @Override
    public String getName() {
        return "UseCaseSeeder";
    }

    @Override
    public Integer getOrder() {
        return 99;
    }
}
