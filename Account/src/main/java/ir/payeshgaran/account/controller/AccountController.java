package ir.payeshgaran.account.controller;

import ir.payeshgaran.account.entity.Account;
import ir.payeshgaran.account.model.AccountModel;
import ir.payeshgaran.account.model.PersonModel;
import ir.payeshgaran.account.service.imp.AccountServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/account")
public record AccountController(AccountServiceImp accountService) {


    @PostMapping()
    public void addAccount(@RequestBody AccountModel accountModel, @RequestHeader("Authorization") String token, HttpServletResponse response) throws IOException {

        accountService.addAccount(accountModel, token, response);
    }

    @GetMapping
    public ResponseEntity<PersonModel> getAllMyAccounts(@RequestHeader("Authorization") String token, HttpServletResponse response) throws IOException {

        PersonModel personModel = accountService.findPersonDetails(token, response);
        return ResponseEntity.ok().body(personModel);


    }

    @GetMapping("/get-id/{accountNumber}")
    public AccountModel getId(@PathVariable("accountNumber") String accountNumber) {
        return accountService.getIdByAccountNumber(accountNumber);
    }

    @GetMapping("/remain/{account_number}")
    public ResponseEntity<Double> getRemainMoney(@PathVariable("account_number") String accountNumber, HttpServletResponse response, @RequestHeader("Authorization") String token) throws IOException {
        Account account = (Account) accountService.findAccountByAccountNumber(accountNumber, token, response);
        assert account != null;
        return ResponseEntity.ok().body(account.getBalance());
    }

    @GetMapping("/remain-all-money/{username}")
    public ResponseEntity<Double> getAllMoney(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(accountService.getAllMoneyByUsername(username));

    }

    @GetMapping("/update-balance/{depositor}/{receiver}/{amount}")
    public boolean updateBalance(@PathVariable("depositor") Long deposit, @PathVariable("receiver") Long receive, @PathVariable Double amount) {
        return accountService.updateBalance(deposit, receive, amount);

    }

    @GetMapping("/get-10-least-transactions/{accountNumber}")
    public ResponseEntity<?> get10LeastTransactions(@PathVariable String accountNumber, HttpServletResponse response, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(accountService.get10LeastTransactions(accountNumber, response, token));

    }
}
