package sit.int221.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.entities.User;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);

    User findByName(String name);

    User findByEmail(String email);
}
