package pl.leastsquaresalgorithms.userservice.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.util.Collection;

@Entity
@Table(name = "role_user")
@EqualsAndHashCode
public class RoleUserEntity {
    private BigInteger roleUserId;
    private String code;
    private String name;
    private Collection<RoleUserToUserEntity> roleUserToUsersByRoleUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_user_id")
    public BigInteger getRoleUserId() {
        return roleUserId;
    }

    public void setRoleUserId(BigInteger roleUserId) {
        this.roleUserId = roleUserId;
    }

    @Basic
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "roleUserByRoleUserId")
    public Collection<RoleUserToUserEntity> getRoleUserToUsersByRoleUserId() {
        return roleUserToUsersByRoleUserId;
    }

    public void setRoleUserToUsersByRoleUserId(Collection<RoleUserToUserEntity> roleUserToUsersByRoleUserId) {
        this.roleUserToUsersByRoleUserId = roleUserToUsersByRoleUserId;
    }
}
