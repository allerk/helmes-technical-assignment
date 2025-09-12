package ee.allerk.helmes_technical_assignment.repository.form;

import ee.allerk.helmes_technical_assignment.model.form.User;
import ee.allerk.helmes_technical_assignment.repository.common.EntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends EntityRepository<User> {
    @Query("SELECT u from User u WHERE u.name=:name")
    Optional<User> findByName(String name);
}
