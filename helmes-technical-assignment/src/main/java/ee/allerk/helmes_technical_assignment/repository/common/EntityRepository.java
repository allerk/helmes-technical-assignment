package ee.allerk.helmes_technical_assignment.repository.common;

import ee.allerk.helmes_technical_assignment.model.common.AbstractEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface EntityRepository <T extends AbstractEntity> extends JpaRepository<T,Long> {
    @Override
    @Transactional
    @Modifying
    @Query("delete from #{#entityName} t where t.id = :id")
    void deleteById(@Param("id") Long id);
}
