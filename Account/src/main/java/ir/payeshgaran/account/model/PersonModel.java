package ir.payeshgaran.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<AccountModel> accountModels;
}

