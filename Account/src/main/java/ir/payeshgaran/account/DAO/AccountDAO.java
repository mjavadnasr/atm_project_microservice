package ir.payeshgaran.account.DAO;


import ir.payeshgaran.account.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class AccountDAO {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    public void addAccount(Account account) {
        hibernateTemplate.saveOrUpdate(account);
    }

    public List<?> findAccountByAccountNumber(String accountNumber) {
        return hibernateTemplate.find("from Account a where a.accountNumber = '" + accountNumber + "'");

    }

    public List<?> findAccountByUsername(String username) {
        return hibernateTemplate.find("from Account a where a.username = '" + username + "'");
    }

    public List<?> findAccountById(Long id) {
        return hibernateTemplate.find("from Account a where a.id = '" + id + "'");
    }

}
