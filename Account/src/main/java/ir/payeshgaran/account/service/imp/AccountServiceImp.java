package ir.payeshgaran.account.service.imp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.payeshgaran.account.DAO.AccountDAO;
import ir.payeshgaran.account.entity.Account;
import ir.payeshgaran.account.model.AccountModel;
import ir.payeshgaran.account.model.PersonModel;
import ir.payeshgaran.account.model.TransactionModel;
import ir.payeshgaran.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Transactional
public class AccountServiceImp implements AccountService {


    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addAccount(AccountModel accountModel, String token, HttpServletResponse response) throws IOException {

        if (token == null) {
            showError("\"TOKEN IS EMPTY\"", response);
        }

        String username = verifyToken(token);

        if (!username.isEmpty()) {
            Account account = Account.builder()
                    .accountNumber(accountModel.getAccountNumber())
                    .balance(accountModel.getBalance())
                    .username(username)
                    .build();
            accountDAO.addAccount(account);
        } else {
            showError("LOGIN AGAIN", response);
        }
    }

    public Object findAccountByAccountNumber(String accountNumber, String token, HttpServletResponse response) throws IOException {
        String username = verifyToken(token);
        if (!username.isEmpty()) {
            List accounts = accountDAO.findAccountByAccountNumber(accountNumber);
            if (username.equals(((Account) (accounts.get(0))).getUsername()))
                return accounts.get(0);
        }
        showError("LOGIN AGAIN", response);
        return null;
    }

    @Override
    public AccountModel getIdByAccountNumber(String accountNumber) {
        List accounts = accountDAO.findAccountByAccountNumber(accountNumber);
        Account account = (Account) accounts.get(0);
        AccountModel accountModel = new AccountModel(account.getId(), account.getBalance(), account.getUsername());
        return accountModel;
    }

    @Override
    public List<?> findAccountByUsername(String token, HttpServletResponse response) throws IOException {
        String username = verifyToken(token);
        if (!username.isEmpty()) {
            List accounts = accountDAO.findAccountByUsername(username);
            return accounts;
        }
        showError("LOGIN AGAIN", response);
        return null;
    }

    @Override
    public Boolean updateBalance(Long deposit, Long receive, Double amount) {
        List depositors = accountDAO.findAccountById(deposit);
        Account depositor = (Account) depositors.get(0);
        depositor.setBalance(depositor.getBalance() - amount);

        List receivers = accountDAO.findAccountById(receive);
        Account receiver = (Account) receivers.get(0);
        receiver.setBalance(receiver.getBalance() + amount);

        return true;
    }

    @Override
    public List get10LeastTransactions(String accountNumber, HttpServletResponse response, String token) {
        List accounts = accountDAO.findAccountByAccountNumber(accountNumber);
        Long id = ((Account) accounts.get(0)).getId();
        String url = "https://TRANSACTION/transaction/get-10-least-transactions/{id}";
        Map<String, Long> param = new HashMap<>();
        param.put("id", id);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        List<TransactionModel> transactionModels = restTemplate.getForObject(builder.buildAndExpand(param).toUri(), List.class);
        return transactionModels;
    }

    private String verifyToken(String authorizationHeader) {
        String username = "";
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                username = decodedJWT.getSubject();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return username;
    }

    public PersonModel findPersonDetails(String token, HttpServletResponse response) throws IOException {
        String username = verifyToken(token);
        if (!username.isEmpty()) {
            String url = "http://PERSON/person/get_details/{username}";
            Map<String, String> urlParams = new HashMap<>();
            urlParams.put("username", username);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
            PersonModel personModel = restTemplate.getForObject(builder.buildAndExpand(urlParams).toUri(), PersonModel.class);
            List<Account> accounts = (List<Account>) accountDAO.findAccountByUsername(username);
            List<AccountModel> accountModels = new ArrayList<>();

            for (Account account : accounts) {
                accountModels.add(new AccountModel(account.getAccountNumber(), account.getBalance()));
            }

            personModel.setAccountModels(accountModels);

            return personModel;
        }
        showError("LOGIN AGAIN", response);

        return null;
    }

    @Override
    public Double getAllMoneyByUsername(String username) {
        double sum = 0;
        List<Account> accounts = (List<Account>) accountDAO.findAccountByUsername(username);

        for (Account account : accounts) {
            sum += account.getBalance();
        }
        return sum;
    }

    private void showError(String message, HttpServletResponse response) throws IOException {
        Map<String, String> errors = new HashMap<>();
        errors.put("ERROR", message);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), errors);
    }
}
