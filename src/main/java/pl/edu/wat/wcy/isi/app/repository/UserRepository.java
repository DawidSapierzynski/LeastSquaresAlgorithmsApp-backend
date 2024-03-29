package pl.edu.wat.wcy.isi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndLoginNot(String email, String login);

    Optional<UserEntity> findByLoginOrEmail(String login, String email);

    Optional<UserEntity> findByUserIdAndDeleted(long userId, byte deleted);
}
