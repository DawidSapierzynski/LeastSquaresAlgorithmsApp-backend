package pl.edu.wat.wcy.isi.app.dto.message.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class JwtResponse {
    private String accessToken;
    private String tokenType;
    private String username;
    private String userId;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtResponse(String accessToken, String username, String userId, Collection<? extends GrantedAuthority> authorities) {
        this.accessToken = accessToken;
        this.username = username;
        this.userId = userId;
        this.authorities = authorities;
        this.tokenType = "Bearer";
    }
}