package ir.payeshgaran.transaction.DAO;

import ir.payeshgaran.transaction.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class TransactionDAO {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void addTransaction(Transaction transaction) {
        hibernateTemplate.save(transaction);
    }

    public List<?> allTransactionsBtDepositorId(Long id) {
        return hibernateTemplate.find("from Transaction t where t.depositor = '" + id + "'");
    }

    public List<?> get10LeastTransactions(Long id) {
        hibernateTemplate.setMaxResults(10);
        return hibernateTemplate.find("from Transaction t where t.depositor = '" + id + "' or t.receiver = '" + id + "'  order by t.id desc ");
    }

}
