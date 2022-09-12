package ir.payeshgaran.account.service;

import ir.payeshgaran.account.model.AccountModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface AccountService {

    void addAccount(AccountModel accountModel, String token, HttpServletResponse response) throws IOException;

    Object findAccountByAccountNumber(String accountNumber, String token, HttpServletResponse response) throws IOException;

    List<?> findAccountByUsername(String token, HttpServletResponse response) throws IOException;

    Double getAllMoneyByUsername(String username);

    AccountModel getIdByAccountNumber(String accountNumber);

    Boolean updateBalance(Long deposit, Long receive, Double amount);

    List get10LeastTransactions(String accountNumber, HttpServletResponse response, String token);
}

