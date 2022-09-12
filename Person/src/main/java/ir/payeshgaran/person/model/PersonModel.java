package ir.payeshgaran.person.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonModel {
    private String username;
    private String password;
    private String type;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String companyName;
    private String companyCode;

    public PersonModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

