package pl.leastsquaresalgorithms.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class LoginForm {
    @NotBlank
    @Size(min = 1, max = 100)
    private String username;

    @NotBlank
    @Size(min = 1, max = 60)
    private String password;
}