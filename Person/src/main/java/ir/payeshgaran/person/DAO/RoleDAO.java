package ir.payeshgaran.person.DAO;


import ir.payeshgaran.person.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class RoleDAO {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void addRole(Role role) {
        hibernateTemplate.save(role);
    }

    public List<?> getRoleByName(String name) {
        return hibernateTemplate.find("from Role r where r.name = '" + name + "'");
    }
}
