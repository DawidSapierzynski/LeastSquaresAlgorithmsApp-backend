package pl.edu.wat.wcy.isi.app.dto.message.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.wat.wcy.isi.app.dto.RoleUserDTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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

    private Set<RoleUserDTO> role;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;
}