package pl.least_squares_algorithms.user_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleUserDto {
    private Long id;
    private String code;
    private String name;
}
