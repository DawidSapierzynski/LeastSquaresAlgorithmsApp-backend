package pl.edu.wat.wcy.isi.app.dto.message.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Builder
public class JwtResponse {
    public static final String BEARER_TOKEN_TYPE = "Bearer";

    private String accessToken;
    private String tokenType;
    private String username;
    private String userId;
    private final Collection<? extends GrantedAuthority> authorities;
}