package ir.payeshgaran.person.service;

import ir.payeshgaran.person.entity.Person;
import ir.payeshgaran.person.model.PersonModel;

import java.util.List;

public interface PersonService {
    void save(PersonModel personModel);

    PersonModel getPersonByUsername(String username);

    String getType(String username);

    String sendPassword(String username, PersonModel personModel);

    void updateScore(String username, int score);

    int showScore(String username);
}
