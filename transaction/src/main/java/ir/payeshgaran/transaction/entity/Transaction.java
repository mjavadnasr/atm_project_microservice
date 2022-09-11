package ir.payeshgaran.transaction.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
@Builder
public class Transaction extends BaseEntity {
    private Long depositor;
    private Long receiver;
    private Double amount;

    @Column(name = "type")
    private String type;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private Date createdAt;

    public Transaction(Long depositor, Long receiver, Double amount, String type) {
        this.depositor = depositor;
        this.receiver = receiver;
        this.amount = amount;
        this.type = type;
    }
}
