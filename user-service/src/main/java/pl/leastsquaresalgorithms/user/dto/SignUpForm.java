package pl.leastsquaresalgorithms.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class SignUpForm {
    @NotBlank
    @Size(min = 3, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 50)
    private String lastName;

    @NotBlank
    @Size(min = 8, max = 50)
    private String login;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    private Set<RoleUserDto> role;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;
}