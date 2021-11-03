package pl.edu.wat.wcy.isi.app.dto;

import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUserDTO {
    private BigInteger id;
    private String code;
    private String name;
}
