package ir.payeshgaran.person.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "real")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Real extends BaseEntity {

    private String firstName;
    private String lastName;
    private String nationalCode;
    @OneToOne
    @JoinColumn
    private Person person;
}
