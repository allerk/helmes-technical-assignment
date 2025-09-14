package ee.allerk.helmes_technical_assignment.service.form;

import ee.allerk.helmes_technical_assignment.dto.form.SectorDto;
import ee.allerk.helmes_technical_assignment.mapper.form.SectorMapper;
import ee.allerk.helmes_technical_assignment.model.form.Sector;
import ee.allerk.helmes_technical_assignment.repository.form.SectorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class SectorService {
    private final SectorRepository sectorRepository;
    private final SectorMapper sectorMapper;

    @Transactional(readOnly = true)
    public List<SectorDto> findAll() {
        List<Sector> sectors = sectorRepository.findAll();
        return sectorMapper.toDtos(sectors);
    }

    @Transactional(readOnly = true)
    public Set<Sector> findByIds(Set<Long> ids) {
        return new HashSet<>(sectorRepository.findByIds(ids));
    }
}
