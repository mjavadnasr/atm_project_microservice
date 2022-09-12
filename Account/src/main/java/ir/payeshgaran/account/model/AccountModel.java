package ir.payeshgaran.account.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountModel {
    private Long id;
    private String accountNumber;
    private String username;
    private double balance;

    public AccountModel(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public AccountModel(Long id, double balance, String username) {
        this.id = id;
        this.balance = balance;
        this.username = username;
    }
}
