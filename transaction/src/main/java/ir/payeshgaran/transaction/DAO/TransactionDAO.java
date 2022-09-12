package ir.payeshgaran.transaction.DAO;

import ir.payeshgaran.transaction.entity.Transaction;
import ir.payeshgaran.transaction.model.Queries;
import ir.payeshgaran.transaction.model.Query;
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
public class TransactionDAO {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void addTransaction(Transaction transaction) {
        hibernateTemplate.save(transaction);
    }

    @SneakyThrows
    public List<?> allTransactionsBtDepositorId(Long id) {
        String query = findQuery("allTransactionsBtDepositorId");
        return hibernateTemplate.find(query, new Long[]{id});
    }

    @SneakyThrows
    public List<?> get10LeastTransactions(Long id) {
        hibernateTemplate.setMaxResults(10);
        String query = findQuery("get10LeastTransactions");
        return hibernateTemplate.find(query, new Long[]{id, id});
    }

    public String findQuery(String key) throws JAXBException {

        File file = new File("config-server/src/main/resources/config/query/transaction.xml");
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
