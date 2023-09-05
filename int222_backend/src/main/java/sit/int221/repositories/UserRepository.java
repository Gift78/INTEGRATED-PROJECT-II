package sit.int221.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.int221.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT count(*) FROM User u WHERE u.username = :username")
    Integer countByUsername(String username);

    @Query("SELECT count(*) FROM User u WHERE u.email = :email")
    Integer countByEmail(String email);

    @Query("SELECT count(*) FROM User u WHERE u.name = :name")
    Integer countByName(String name);

    User findByUsername(String string);

    Integer countByEmailAndIdNot(String string, Integer id);

    Integer countByNameAndIdNot(String string, Integer id);

    User findByEmail(String string);

    Integer countByUsernameAndIdNot(String string, Integer id);

    User findByName(String string);
}
