package ir.payeshgaran.person.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "legal")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Legal extends BaseEntity {

    private String name;
    private String code;
    @OneToOne
    @JoinColumn
    private Person person;

}
