package pl.leastsquaresalgorithms.user.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Entity
@Table(name = "role_user")
@Data
@EqualsAndHashCode
public class RoleUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_user_id")
    private Long roleUserId;

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "roleUserByRoleUserId")
    private Collection<RoleUserToUserEntity> roleUserToUsersByRoleUserId;
}
