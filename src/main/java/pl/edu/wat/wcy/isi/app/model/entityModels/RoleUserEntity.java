package pl.edu.wat.wcy.isi.app.model.entityModels;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "role_user", schema = "aaa")
public class RoleUserEntity {
    private long roleUserId;
    private String code;
    private String name;
    private Collection<RoleUserToUserEntity> roleUserToUsersByRoleUserId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_user_id")
    public long getRoleUserId() {
        return roleUserId;
    }

    public void setRoleUserId(long roleUserId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleUserEntity that = (RoleUserEntity) o;
        return roleUserId == that.roleUserId &&
                Objects.equals(code, that.code) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleUserId, code, name);
    }

    @OneToMany(mappedBy = "roleUserByRoleUserId")
    public Collection<RoleUserToUserEntity> getRoleUserToUsersByRoleUserId() {
        return roleUserToUsersByRoleUserId;
    }

    public void setRoleUserToUsersByRoleUserId(Collection<RoleUserToUserEntity> roleUserToUsersByRoleUserId) {
        this.roleUserToUsersByRoleUserId = roleUserToUsersByRoleUserId;
    }
}
