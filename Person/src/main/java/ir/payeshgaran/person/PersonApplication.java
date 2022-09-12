package ir.payeshgaran.person;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import ir.payeshgaran.person.DAO.RoleDAO;
import ir.payeshgaran.person.entity.Person;
import ir.payeshgaran.person.entity.Role;
import ir.payeshgaran.person.service.imp.PersonServiceImp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableEurekaClient
@OpenAPIDefinition(info = @Info(title = "Person Module"))

public class PersonApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner runner(RoleDAO roleDAO, PersonServiceImp personServiceImp) {
        return args -> {

            Role userRole = new Role("USER");
            Role adminRole = new Role("ADMIN");
            roleDAO.addRole(adminRole);
            roleDAO.addRole(userRole);
            Person admin = new Person("admin", "admin", "admin");
            personServiceImp.saveAdmin(admin);
            personServiceImp.addRoleToPerson(admin, adminRole);
        };


    }
}
