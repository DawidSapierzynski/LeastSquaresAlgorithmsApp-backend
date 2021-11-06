package pl.edu.wat.wcy.isi.app.model.entityModels;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "role_user_to_user")
@EqualsAndHashCode
public class RoleUserToUserEntity {
    private BigInteger roleUserToUserId;
    private UserEntity userByUserId;
    private RoleUserEntity roleUserByRoleUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_user_to_user_id")
    public BigInteger getRoleUserToUserId() {
        return roleUserToUserId;
    }

    public void setRoleUserToUserId(BigInteger roleUserToUserId) {
        this.roleUserToUserId = roleUserToUserId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    public UserEntity getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserEntity userByUserId) {
        this.userByUserId = userByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "role_user_id", referencedColumnName = "role_user_id", nullable = false)
    public RoleUserEntity getRoleUserByRoleUserId() {
        return roleUserByRoleUserId;
    }

    public void setRoleUserByRoleUserId(RoleUserEntity roleUserByRoleUserId) {
        this.roleUserByRoleUserId = roleUserByRoleUserId;
    }
}
