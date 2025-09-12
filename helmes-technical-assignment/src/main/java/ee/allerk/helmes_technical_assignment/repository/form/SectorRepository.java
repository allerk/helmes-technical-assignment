package ee.allerk.helmes_technical_assignment.repository.form;

import ee.allerk.helmes_technical_assignment.repository.common.EntityRepository;
import ee.allerk.helmes_technical_assignment.model.form.Sector;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorRepository extends EntityRepository<Sector> {
    @Query("SELECT s from Sector s JOIN s.users u WHERE u.id = :userId")
    List<Sector> findAllByUserId(Long userId);

    @Query("SELECT s FROM Sector s WHERE s.id IN :ids")
    List<Sector> findByIds(List<Long> ids);
}
