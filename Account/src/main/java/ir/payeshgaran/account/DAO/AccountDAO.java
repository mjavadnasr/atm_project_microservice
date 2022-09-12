package ir.payeshgaran.account.DAO;


import ir.payeshgaran.account.entity.Account;
import ir.payeshgaran.account.model.Queries;
import ir.payeshgaran.account.model.Query;
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
public class AccountDAO {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void addAccount(Account account) {
        hibernateTemplate.saveOrUpdate(account);
    }

    @SneakyThrows
    public List<?> findAccountByAccountNumber(String accountNumber) {
        String query = findQuery("findAccountByAccountNumber");
        return hibernateTemplate.find(query, new String[]{accountNumber});

    }

    @SneakyThrows
    public List<?> findAccountByUsername(String username) {
        String query = findQuery("findAccountByAccountNumber");
        return hibernateTemplate.find(query, new String[]{username});
    }

    @SneakyThrows
    public List<?> findAccountById(Long id) {
        String query = findQuery("findAccountById");
        return hibernateTemplate.find(query, new Long[]{id});
    }

    public String findQuery(String key) throws JAXBException {

        File file = new File("config-server/src/main/resources/config/query/account.xml");
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
