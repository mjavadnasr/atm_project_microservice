package ir.payeshgaran.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AccountModel {
    private Long id;
    private String accountNumber;
    private Double balance;
    private String username;

    public AccountModel(Long id, Double balance) {
        this.id = id;
        this.balance = balance;
    }
}
