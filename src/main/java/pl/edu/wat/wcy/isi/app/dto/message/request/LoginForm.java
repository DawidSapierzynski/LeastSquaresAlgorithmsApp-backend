package pl.edu.wat.wcy.isi.app.dto.message.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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