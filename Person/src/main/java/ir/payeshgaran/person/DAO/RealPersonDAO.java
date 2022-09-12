package ir.payeshgaran.person.DAO;

import ir.payeshgaran.person.entity.Person;
import ir.payeshgaran.person.entity.Real;
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
public class RealPersonDAO {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void add(Real real) {
        hibernateTemplate.save(real);
    }

    @SneakyThrows
    public List<?> getRealById(Long id) {
        return hibernateTemplate.find("from Real r where r.person = '"+id+"'");
    }


}
