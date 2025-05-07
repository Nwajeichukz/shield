package store.management.store_system.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class RegistrationDto {
    @NotEmpty(message = "firstName should not be blank")
    private String firstName;

    @NotEmpty(message = "lastName should not be blank")
    private String lastName;

    @NotEmpty(message = "email should not be blank")
    private String email;

    @javax.validation.constraints.Pattern(
            regexp = "MANAGER|SALES",
            message = "Department type should be either manager, sales,"
    )    private String department;


    @NotEmpty(message = "password should not be blank")
    private String password;

    @NotEmpty(message = "confirmPassword should not be blank")
    private String confirmPassword;

}
