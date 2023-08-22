package sit.int221.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.entities.User;

public interface UserRepositttory extends JpaRepository<User,Integer> {
}
