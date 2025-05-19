package app.calendar_service.web.seeders;

import app.calendar_service.domain.entities.MemberRole;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Para ejecutar todos los seeders en orden:
 *          ./mvnw spring-boot:run -Dspring-boot.run.arguments=--seedAll
 *
 * Para ejecutar una lista de seeders:
 *          ./mvnw spring-boot:run -Dspring-boot.run.arguments="--seed=UserSeeder,SystemRoleSeeder,EtcSeeder..."
 */

@Component
public class DatabaseSeeder implements ApplicationRunner {

    private final Map<String,Seeder> seederMap = new HashMap<>();

    public DatabaseSeeder(
            UserSeeder userSeeder,
            SystemRoleSeeder systemRoleSeeder,
            GroupSeeder groupSeeder,
            MemberRoleSeeder memberRoleSeeder,
            MembershipSeeder membershipSeeder,
            UseCaseSeeder useCaseSeeder
    ){
        seederMap.put(userSeeder.getName(), userSeeder);
        seederMap.put(systemRoleSeeder.getName(), systemRoleSeeder);
        seederMap.put(groupSeeder.getName(), groupSeeder);
        seederMap.put(memberRoleSeeder.getName(), memberRoleSeeder);
        seederMap.put(membershipSeeder.getName(), membershipSeeder);
        seederMap.put(useCaseSeeder.getName(), useCaseSeeder);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(args.containsOption("seed")){
            args.getOptionValues("seed");
            seedSome(args);

        } else if (args.containsOption("seedAll")) {
            seedAll();

        }
    }

    public void seedAll(){
        List<Seeder> seeders = new ArrayList<>(seederMap.values());

        seeders.sort(Comparator.comparing(Seeder::getOrder));

        seeders.forEach(Seeder::seed);
    }

    private void seedSome(ApplicationArguments args){
        List<String> seederNames = args.getOptionValues("seed");

        seederNames.forEach(seederName -> Optional.ofNullable(seederMap.get(seederName))
                .ifPresent(Seeder::seed)
        );
    }
}
