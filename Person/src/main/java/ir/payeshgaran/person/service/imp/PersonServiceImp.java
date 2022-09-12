package ir.payeshgaran.person.service.imp;

import ir.payeshgaran.person.DAO.LegalPersonDAO;
import ir.payeshgaran.person.DAO.PersonDAO;
import ir.payeshgaran.person.DAO.RealPersonDAO;
import ir.payeshgaran.person.DAO.RoleDAO;
import ir.payeshgaran.person.entity.Legal;
import ir.payeshgaran.person.entity.Person;
import ir.payeshgaran.person.entity.Real;
import ir.payeshgaran.person.entity.Role;
import ir.payeshgaran.person.model.PersonModel;
import ir.payeshgaran.person.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@Slf4j
public record PersonServiceImp(PersonDAO personDAO, RealPersonDAO realPersonDAO,
                               LegalPersonDAO legalPersonDAO, RoleDAO roleDAO,
                               PasswordEncoder passwordEncoder,
                               RestTemplate restTemplate) implements PersonService, UserDetailsService {
    @Override
    public void save(PersonModel personModel) {
        Role role = (Role) roleDAO.getRoleByName("USER").get(0);
        Person person = Person.builder()
                .username(personModel.getUsername())
                .password(passwordEncoder.encode(personModel.getPassword()))
                .type(personModel.getType())
                .role(role)
                .build();
        personDAO.addPerson(person);
        addRoleToPerson(person, role);
        if (personModel.getType().equals("real") && isValidRealPerson(personModel)) {

            Real realPerson = Real.builder()
                    .firstName(personModel.getFirstName())
                    .lastName(personModel.getLastName())
                    .nationalCode(personModel.getNationalCode())
                    .person(person)
                    .build();

            realPersonDAO.add(realPerson);

        } else if (personModel.getType().equals("legal") && isValidLegalPerson(personModel)) {

            Legal legalPerson = Legal.builder()
                    .name(personModel.getCompanyName())
                    .code(personModel.getCompanyCode())
                    .person(person)
                    .build();

            legalPersonDAO.add(legalPerson);

        } else
            throw new RuntimeException();
    }

    public boolean isValidRealPerson(PersonModel personModel) {
        if (personModel.getFirstName() != null && personModel.getLastName() != null && personModel.getNationalCode() != null)
            return true;
        return false;
    }

    public boolean isValidLegalPerson(PersonModel personModel) {
        if (personModel.getCompanyName() != null && personModel.getCompanyCode() != null)
            return true;
        return false;
    }

    @Override
    public PersonModel getPersonByUsername(String username) {
        List persons = personDAO.getPersonByUsername(username);
        Person person = (Person) persons.get(0);
        Long id = person.getId();
        List<?> reals = realPersonDAO.getRealById(id);
        List<?> legals = legalPersonDAO.getLegalById(id);

        if (legals.size() != 0) {
            Legal legal = (Legal) legals.get(0);

            PersonModel personModel = PersonModel.builder()
                    .username(person.getUsername())
                    .password(person.getPassword())
                    .companyName(legal.getName())
                    .companyCode(legal.getCode())
                    .build();
            return personModel;

        } else if (reals.size() != 0) {
            Real real = (Real) reals.get(0);

            PersonModel personModel = PersonModel.builder()
                    .username(person.getUsername())
                    .password(person.getPassword())
                    .firstName(real.getFirstName())
                    .lastName(real.getLastName())
                    .nationalCode(real.getNationalCode())
                    .build();
            return personModel;

        }
        return null;
    }

    @Override
    public String getType(String username) {
        return personDAO.getType(username);
    }

    @Override
    public String sendPassword(String username, PersonModel personModel) {
        Person person = ((Person) personDAO.getPersonByUsername(username).get(0));
        if (person.getType().equals("legal")) {
            Legal legal = ((Legal) legalPersonDAO.getLegalById(person.getId()).get(0));
            if (legal.getName().equals(personModel.getCompanyName()))
                if (legal.getCode().equals(personModel.getCompanyCode())) {
                    String newPass = generateRandomPassword();
                    person.setPassword(passwordEncoder.encode(newPass));
                    personDAO.addPerson(person);
                    return newPass;
                }
            return "BAD CONDITIONAL TY AGAIN LATER";

        } else {
            Real real = ((Real) realPersonDAO.getRealById(person.getId()).get(0));
            if (real.getLastName().equals(personModel.getLastName()))
                if (real.getNationalCode().equals(personModel.getNationalCode())) {
                    String newPass = generateRandomPassword();
                    person.setPassword(passwordEncoder.encode(newPass));
                    personDAO.addPerson(person);
                    return newPass;
                }
            return "BAD CONDITIONAL TY AGAIN LATER";
        }
    }

    @Override
    public void updateScore(String username, int score) {
        Person person = ((Person) personDAO.getPersonByUsername(username).get(0));
        person.setScore(score + person.getScore());
        personDAO.addPerson(person);
    }

    @Override
    public int showScore(String username) {
        Person person = ((Person) personDAO.getPersonByUsername(username).get(0));
        return person.getScore();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List persons = personDAO.getPersonByUsername(username);
        if (persons == null)
            throw new UsernameNotFoundException("Person Not Found");
        Person person = (Person) persons.get(0);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(person.getRole().getName()));

        return new User(person.getUsername(), person.getPassword(), authorities);
    }

    public void saveAdmin(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personDAO.addPerson(person);
    }

    public void addRoleToPerson(Person person, Role role) {
        personDAO.addRoleToPerson(person, role);
    }

    public Double getRemainMoney(String username) {
        if (!username.isEmpty()) {
            String url = "http://ACCOUNT/account/remain-all-money/{username}";
            Map<String, String> urlParams = new HashMap<>();
            urlParams.put("username", username);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
            Double balance = restTemplate.getForObject(builder.buildAndExpand(urlParams).toUri(), Double.class);

            return balance;
        } else {
            return -1.0;
        }
    }

    public String generateRandomPassword() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
