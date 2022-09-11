package ir.payeshgaran.transaction.service.imp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.payeshgaran.transaction.DAO.TransactionDAO;
import ir.payeshgaran.transaction.entity.Transaction;
import ir.payeshgaran.transaction.model.AccountModel;
import ir.payeshgaran.transaction.model.TransactionModel;
import ir.payeshgaran.transaction.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public record TransactionServiceImp(TransactionDAO transactionDAO,
                                    RestTemplate restTemplate) implements TransactionService {

    @Override
    public void addTransaction(TransactionModel transactionModel, String token, HttpServletResponse response) throws IOException {
        String username = verifyToken(token);
        if (username.isEmpty()) {
            showError("LOGIN AGAIN", response);
            return;
        }

        String urlForPersonType = "http://PERSON/person/get-type/{username}";
        Map<String, String> personParams = new HashMap<>();
        personParams.put("username", username);
        UriComponentsBuilder personBuilder = UriComponentsBuilder.fromUriString(urlForPersonType);
        String personType = restTemplate.getForObject(personBuilder.buildAndExpand(personParams).toUri(), String.class);

        String url = "http://ACCOUNT/account/get-id/{accountNumber}";
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("accountNumber", transactionModel.getDepositor());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        AccountModel depositor = restTemplate.getForObject(builder.buildAndExpand(urlParams).toUri(), AccountModel.class);
        if (!username.equals(depositor.getUsername()))
        {
            showError("this is not your account", response);
            return;
        }
        List<Transaction> transactions = (List<Transaction>) transactionDAO.allTransactionsBtDepositorId(depositor.getId());
        double sum = 0;

        for (Transaction transaction : transactions) {
            if (LocalDate.now().getDayOfMonth() == transaction.getCreatedAt().getDate())
                sum += transaction.getAmount();
        }

        System.out.println(sum);
        System.out.println("***************************************************************************");
        if (transactionModel.getType().equals("normal")) {


            if (sum + transactionModel.getAmount() < 10000000.0 || personType.equals("legal")) {
                String url1 = "http://ACCOUNT/account/get-id/{accountNumber}";
                Map<String, String> urlParams1 = new HashMap<>();
                urlParams1.put("accountNumber", transactionModel.getReceiver());
                UriComponentsBuilder builder1 = UriComponentsBuilder.fromUriString(url);
                AccountModel receiver = restTemplate.getForObject(builder1.buildAndExpand(urlParams1).toUri(), AccountModel.class);

                if (depositor.getBalance() > transactionModel.getAmount()) {
                    Transaction transaction = Transaction.builder()
                            .depositor(depositor.getId())
                            .receiver(receiver.getId())
                            .amount(transactionModel.getAmount())
                            .type(transactionModel.getType())
                            .build();

                    transactionDAO.addTransaction(transaction);

                    String updateScore = "http://PERSON/person/update-score/{username}/{score}";
                    Map mapScore = new HashMap();
                    mapScore.put("username", username);
                    mapScore.put("score", calculateScore(transaction.getAmount(), transactionModel.getType(), personType));
                    UriComponentsBuilder builderScore = UriComponentsBuilder.fromUriString(updateScore);
                    restTemplate.getForObject(builderScore.buildAndExpand(mapScore).toUri(), Boolean.class);


                    String updateBalance = "http://ACCOUNT/account/update-balance/{depositor}/{receiver}/{amount}";
                    Map urlParams2 = new HashMap<>();

                    urlParams2.put("depositor", depositor.getId());
                    urlParams2.put("receiver", receiver.getId());
                    urlParams2.put("amount", transactionModel.getAmount());
                    UriComponentsBuilder builder2 = UriComponentsBuilder.fromUriString(updateBalance);


                    Boolean a = restTemplate.getForObject(builder2.buildAndExpand(urlParams2).toUri(), Boolean.class);


                } else {
                    showError("NOT ENOUGH MONEY ", response);
                }
            } else
                showError("Limit 10 million for card 2 card", response);


        } else if (transactionModel.getType().equals("paya")) {


            String url1 = "http://ACCOUNT/account/get-id/{accountNumber}";
            Map<String, String> urlParams1 = new HashMap<>();
            urlParams1.put("accountNumber", transactionModel.getReceiver());
            UriComponentsBuilder builder1 = UriComponentsBuilder.fromUriString(url);
            AccountModel receiver = restTemplate.getForObject(builder1.buildAndExpand(urlParams1).toUri(), AccountModel.class);
            if (personType.equals("real")) {

                if (sum + transactionModel.getAmount() < 50000000.0) {

                    if (depositor.getBalance() > transactionModel.getAmount()) {
                        Transaction transaction = Transaction.builder()
                                .depositor(depositor.getId())
                                .receiver(receiver.getId())
                                .amount(transactionModel.getAmount())
                                .type(transactionModel.getType())
                                .build();
                        transactionDAO.addTransaction(transaction);

                        String updateScore = "http://PERSON/person/update-score/{username}/{score}";
                        Map mapScore = new HashMap();
                        mapScore.put("username", username);
                        mapScore.put("score", calculateScore(transaction.getAmount(), transactionModel.getType(), personType));
                        UriComponentsBuilder builderScore = UriComponentsBuilder.fromUriString(updateScore);
                        restTemplate.getForObject(builderScore.buildAndExpand(mapScore).toUri(), Boolean.class);


                        String updateBalance = "http://ACCOUNT/account/update-balance/{depositor}/{receiver}/{amount}";
                        Map urlParams2 = new HashMap<>();

                        urlParams2.put("depositor", depositor.getId());
                        urlParams2.put("receiver", receiver.getId());
                        urlParams2.put("amount", transactionModel.getAmount());
                        UriComponentsBuilder builder2 = UriComponentsBuilder.fromUriString(updateBalance);


                        Boolean a = restTemplate.getForObject(builder2.buildAndExpand(urlParams2).toUri(), Boolean.class);


                    } else
                        showError("NOT ENOUGH MONEY ", response);


                } else
                    showError("Limit 50 million for paya", response);

            }

            if (personType.equals("legal")) {
                if (depositor.getBalance() > transactionModel.getAmount()) {
                    if (sum + transactionModel.getAmount() < 200000000.0) {

                        Transaction transaction = Transaction.builder()
                                .depositor(depositor.getId())
                                .receiver(receiver.getId())
                                .amount(transactionModel.getAmount())
                                .type(transactionModel.getType())
                                .build();
                        transactionDAO.addTransaction(transaction);

                        String updateScore = "http://PERSON/person/update-score/{username}/{score}";
                        Map mapScore = new HashMap();
                        mapScore.put("username", username);
                        mapScore.put("score", calculateScore(transaction.getAmount(), transactionModel.getType(), personType));
                        UriComponentsBuilder builderScore = UriComponentsBuilder.fromUriString(updateScore);
                        restTemplate.getForObject(builderScore.buildAndExpand(mapScore).toUri(), Boolean.class);

                        String updateBalance = "http://ACCOUNT/account/update-balance/{depositor}/{receiver}/{amount}";
                        Map urlParams2 = new HashMap<>();

                        urlParams2.put("depositor", depositor.getId());
                        urlParams2.put("receiver", receiver.getId());
                        urlParams2.put("amount", transactionModel.getAmount());
                        UriComponentsBuilder builder2 = UriComponentsBuilder.fromUriString(updateBalance);


                        Boolean a = restTemplate.getForObject(builder2.buildAndExpand(urlParams2).toUri(), Boolean.class);
                    } else
                        showError("Limit 200 million for paya", response);


                } else
                    showError("NOT ENOUGH MONEY ", response);
            }


        }


        showError("DONE", response);
    }

    @Override
    public List<TransactionModel> get10LeastTransactions(Long id) {


        return (List<TransactionModel>) transactionDAO.get10LeastTransactions(id);

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
                System.out.println(e);

            }

        }
        return username;

    }


    private void showError(String message, HttpServletResponse response) throws IOException {
        Map<String, String> errors = new HashMap<>();
        errors.put("ERROR", message);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), errors);

    }

    private int calculateScore(double amount, String transactionType, String personType) {
        int score = 0;
        double count = 0;

        if (personType.equals("real")) {
            if (transactionType.equals("normal")) {
                count = amount / 100000;
                score = (int) (count * 2);
            } else if (transactionType.equals("paya")) {
                count = amount / 1000000;
                score = (int) (count * 5);
            }
        } else if (personType.equals("legal")) {
            if (transactionType.equals("normal")) {
                count = amount / 1000000;
                score = (int) (count * 5);
            } else if (transactionType.equals("paya")) {
                count = amount / 10000000;
                score = (int) (count * 10);
            }
        }
        return score;

    }
}
