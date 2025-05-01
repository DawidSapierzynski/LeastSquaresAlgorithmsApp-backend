package pl.leastsquaresalgorithms.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 50)
    private String firstName;
    @NotBlank
    @Size(min = 3, max = 50)
    private String lastName;
    @NotBlank
    @Size(min = 3, max = 50)
    private String login;
    @NotBlank
    @Size(max = 60)
    @Email
    private String email;
    private Collection<RoleUserDto> rolesUserDto;
    private boolean deleted;
    private boolean active;
    private boolean admin;
}
