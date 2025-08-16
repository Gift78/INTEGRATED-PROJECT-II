package sit.int221.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.entities.Subscription;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    Subscription findByEmailAndCategoryId(String email, Integer categoryId);
    List<Subscription> findByCategoryId(Integer categoryId);
    Boolean existsByEmailAndCategoryId(String email, Integer categoryId);
}
