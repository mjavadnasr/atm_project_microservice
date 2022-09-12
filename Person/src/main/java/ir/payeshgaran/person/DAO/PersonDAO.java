package ir.payeshgaran.person.DAO;

import ir.payeshgaran.person.entity.Person;
import ir.payeshgaran.person.entity.Role;
import ir.payeshgaran.person.model.Queries;
import ir.payeshgaran.person.model.Query;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

@Transactional
@Repository
public class PersonDAO {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void addPerson(Person person) {
        hibernateTemplate.saveOrUpdate(person);
    }

    @SneakyThrows
    public List<?> getPersonByUsername(String username) {
        String query = findQuery("getPersonByUsername");
        return hibernateTemplate.find(query, new String[]{username});
    }

    public void addRoleToPerson(Person person, Role role) {
        person.setRole(role);
        addPerson(person);
    }

    @SneakyThrows
    public String getType(String username) {
        String query = findQuery("getType");
        List persons = hibernateTemplate.find(query, new String[]{username});
        return ((Person) persons.get(0)).getType();
    }

    public String findQuery(String key) throws JAXBException {

        File file = new File("config-server/src/main/resources/config/query/person.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(Queries.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Queries rootEl = (Queries) jaxbUnmarshaller.unmarshal(file);

        for (Query query : rootEl.getQueries()) {
            if (query.getKey().equals(key))
                return query.getValue();
        }
        return null;
    }
}
