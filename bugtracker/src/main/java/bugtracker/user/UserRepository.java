package bugtracker.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAll();

    UserEntity findUserEntityById(Long id);

    UserEntity findUserEntityByName(String name);

    List<UserEntity> findByType(Integer type);

    UserEntity findByName(String name);

}
