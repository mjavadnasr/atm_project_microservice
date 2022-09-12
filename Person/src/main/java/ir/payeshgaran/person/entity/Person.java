package ir.payeshgaran.person.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "Person")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person extends BaseEntity {

    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    @Column(name = "type")
    private String type;
    @Column(name = "score")
    private int score = 0;
    @OneToOne
    @JoinColumn
    private Role role;

    public Person(String username, String password, String type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }
}
