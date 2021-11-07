package pl.edu.wat.wcy.isi.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@Builder
public class RoleUserDTO {
    private BigInteger id;
    private String code;
    private String name;
}
