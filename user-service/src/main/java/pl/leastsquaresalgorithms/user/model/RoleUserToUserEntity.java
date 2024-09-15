package pl.leastsquaresalgorithms.user.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "role_user_to_user")
@Data
@EqualsAndHashCode
public class RoleUserToUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_user_to_user_id")
    private Long roleUserToUserId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity userByUserId;

    @ManyToOne
    @JoinColumn(name = "role_user_id", referencedColumnName = "role_user_id", nullable = false)
    private RoleUserEntity roleUserByRoleUserId;
}
