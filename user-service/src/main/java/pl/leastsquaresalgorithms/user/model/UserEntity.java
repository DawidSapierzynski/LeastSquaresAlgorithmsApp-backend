package pl.leastsquaresalgorithms.user.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Basic
    @Column(name = "login")
    private String login;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    private String lastName;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "deleted")
    private Boolean deleted;

    @Basic
    @Column(name = "active")
    private Boolean active;

    @Basic
    @Column(name = "admin")
    private Boolean admin;

    @OneToMany(mappedBy = "userByUserId")
    private Collection<RoleUserToUserEntity> roleUserToUsersByUserId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_user_to_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_user_id"))
    private Collection<RoleUserEntity> rolesUser;
}
