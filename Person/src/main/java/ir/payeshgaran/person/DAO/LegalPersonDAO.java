package ir.payeshgaran.person.DAO;

import ir.payeshgaran.person.entity.Legal;
import ir.payeshgaran.person.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class LegalPersonDAO {


    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void add(Legal legal) {
        hibernateTemplate.save(legal);
    }
    public List<?> getById(Long id)
    {
        return (List<?>) hibernateTemplate.find("from Legal l where l.person ='"+id+"'");

    }
}
