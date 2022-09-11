package ir.payeshgaran.person.DAO;

import ir.payeshgaran.person.entity.Person;
import ir.payeshgaran.person.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class PersonDAO {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void addPerson(Person person) {
        hibernateTemplate.saveOrUpdate(person);
    }

    public List<?> getPersonByUsername(String username) {
        return hibernateTemplate.find("from Person p where p.username = '" + username + "'");
    }

    public void addRoleToPerson(Person person, Role role) {
        person.setRole(role);
        addPerson(person);
    }

    public String getType(String username) {
        List persons = hibernateTemplate.find("from Person p where p.username = '" + username + "'");
        return ((Person) persons.get(0)).getType();
    }
}
