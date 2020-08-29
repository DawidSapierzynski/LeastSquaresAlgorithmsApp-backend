package pl.edu.wat.wcy.isi.app.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUserDTO {
    private long id;
    private String code;
    private String name;
}
