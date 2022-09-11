package ir.payeshgaran.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionModel {
    private String depositor;
    private String receiver;
    private String type;
    private double amount;
}
