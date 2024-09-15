package pl.leastsquaresalgorithms.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private Collection<RoleUserDto> rolesUserDto;
    private boolean deleted;
    private boolean active;
    private boolean admin;
}
