package sit.int221.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.entities.UnsubscribeToken;

@Repository
public interface UnsubscribeTokenRepository extends JpaRepository<UnsubscribeToken, Integer> {
    UnsubscribeToken findByToken(String token);
    UnsubscribeToken findByEmail(String email);
    void deleteByToken(String token);
}
