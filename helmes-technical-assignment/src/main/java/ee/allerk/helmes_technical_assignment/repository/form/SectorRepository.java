package ee.allerk.helmes_technical_assignment.repository.form;

import ee.allerk.helmes_technical_assignment.repository.common.EntityRepository;
import ee.allerk.helmes_technical_assignment.model.form.Sector;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SectorRepository extends EntityRepository<Sector> {
    @Query("SELECT s FROM Sector s WHERE s.id IN :ids")
    List<Sector> findByIds(Set<Long> ids);

    @Override
    @Query("SELECT s FROM Sector s WHERE s.parent IS NULL")
    List<Sector> findAll();
}
