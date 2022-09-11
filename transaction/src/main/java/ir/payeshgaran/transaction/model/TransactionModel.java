package ir.payeshgaran.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionModel {
    private String depositor;
    private String receiver;
    private Double amount;
    private String type;

}
