package ir.payeshgaran.person.entity;

import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "seq")
    Long id;
}
