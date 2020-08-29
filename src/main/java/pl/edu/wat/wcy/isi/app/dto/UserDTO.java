package pl.edu.wat.wcy.isi.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@Builder
public class UserDTO {
    private long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private Collection<RoleUserDTO> rolesUserDto;
    private boolean deleted;
    private boolean active;
    private boolean admin;
}
