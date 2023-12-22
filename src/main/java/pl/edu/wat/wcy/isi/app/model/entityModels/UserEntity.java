package pl.edu.wat.wcy.isi.app.model.entityModels;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.util.Collection;

@Entity
@Table(name = "user")
@EqualsAndHashCode
public class UserEntity {
    private BigInteger userId;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Byte deleted;
    private Byte active;
    private Byte admin;
    private Collection<DataSeriesFileEntity> dataSeriesFilesByUserId;
    private Collection<RoleUserToUserEntity> roleUserToUsersByUserId;
    private Collection<ApproximationPropertiesEntity> approximationPropertiesByUserId;
    private Collection<RoleUserEntity> rolesUser;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "is_deleted")
    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    @Basic
    @Column(name = "is_active")
    public Byte getActive() {
        return active;
    }

    public void setActive(Byte active) {
        this.active = active;
    }

    @Basic
    @Column(name = "is_admin")
    public Byte getAdmin() {
        return admin;
    }

    public void setAdmin(Byte admin) {
        this.admin = admin;
    }

    @OneToMany(mappedBy = "user")
    public Collection<DataSeriesFileEntity> getDataSeriesFilesByUserId() {
        return dataSeriesFilesByUserId;
    }

    public void setDataSeriesFilesByUserId(Collection<DataSeriesFileEntity> dataSeriesFilesByUserId) {
        this.dataSeriesFilesByUserId = dataSeriesFilesByUserId;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<RoleUserToUserEntity> getRoleUserToUsersByUserId() {
        return roleUserToUsersByUserId;
    }

    public void setRoleUserToUsersByUserId(Collection<RoleUserToUserEntity> roleUserToUsersByUserId) {
        this.roleUserToUsersByUserId = roleUserToUsersByUserId;
    }

    @OneToMany(mappedBy = "user")
    public Collection<ApproximationPropertiesEntity> getApproximationPropertiesByUserId() {
        return approximationPropertiesByUserId;
    }

    public void setApproximationPropertiesByUserId(Collection<ApproximationPropertiesEntity> approximationPropertiesByUserId) {
        this.approximationPropertiesByUserId = approximationPropertiesByUserId;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "role_user_to_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_user_id"))
    public Collection<RoleUserEntity> getRolesUser() {
        return rolesUser;
    }

    public void setRolesUser(Collection<RoleUserEntity> rolesUser) {
        this.rolesUser = rolesUser;
    }

    @Transient
    public boolean isAdmin() {
        return getAdmin().equals((byte) 1);
    }
}
