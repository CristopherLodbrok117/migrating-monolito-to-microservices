package app.calendar_service.domain.policies;

import app.calendar_service.application.repositories.MembershipRepository;
import app.calendar_service.application.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PolicyConfig {

    @Bean
    public GroupPolicy groupPolicy(UserRepository userRepository, MembershipRepository membershipRepository) {
        return new GroupPolicy(userRepository, membershipRepository);
    }

//    @Bean
//    public FilePolicy filePolicy(UserRepository userRepository, MembershipRepository membershipRepository) {
//        return new FilePolicy(userRepository, membershipRepository);
//    }

    @Bean
    public EventPolicy eventPolicy(UserRepository userRepository, MembershipRepository membershipRepository) {
        return new EventPolicy(userRepository, membershipRepository);
    }
}
