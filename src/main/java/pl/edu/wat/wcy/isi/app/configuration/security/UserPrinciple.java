package pl.edu.wat.wcy.isi.app.configuration.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrinciple implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final BigInteger id;

    private final String name;

    private final String login;

    private final String email;

    private final boolean isActive;

    private final boolean isDeleted;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(BigInteger id, String name, String login, String email, String password,
                         boolean isActive, boolean isDeleted,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
    }

    public static UserPrinciple build(UserEntity user) {
        List<GrantedAuthority> authorities = user.getRolesUser().stream()
                .map(role -> new SimpleGrantedAuthority(role.getCode()))
                .collect(Collectors.toList());

        return new UserPrinciple(
                user.getUserId(),
                user.getFirstName(),
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getActive().equals((byte) 1),
                user.getDeleted().equals((byte) 1),
                authorities
        );
    }

    public BigInteger getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !this.isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }
}
