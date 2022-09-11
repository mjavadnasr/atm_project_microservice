package ir.payeshgaran.account.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Account extends BaseEntity {
    private String accountNumber;
    private double balance = 10000.0;
    private String username;
}
