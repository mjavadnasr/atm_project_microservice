package ir.payeshgaran.person.DAO;

import ir.payeshgaran.person.entity.Real;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class RealPersonDAO {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void add(Real real) {
        hibernateTemplate.save(real);
    }
    public List<?> getById(Long id)
    {
        return  hibernateTemplate.find("from Real r where r.person ='"+id+"'");

    }
}
