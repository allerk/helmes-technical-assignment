package ee.allerk.helmes_technical_assignment.service.form;

import ee.allerk.helmes_technical_assignment.dto.form.SectorDto;
import ee.allerk.helmes_technical_assignment.mapper.form.SectorMapper;
import ee.allerk.helmes_technical_assignment.model.form.Sector;
import ee.allerk.helmes_technical_assignment.repository.form.SectorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SectorService {
    private final SectorRepository sectorRepository;
    private final SectorMapper sectorMapper;

    @Transactional
    public List<SectorDto> findAll() {
        List<Sector> sectors = sectorRepository.findAll();
        return sectorMapper.toDtos(sectors);
    }

    @Transactional
    public List<SectorDto> findUserSectors(Long userId) {
        List<Sector> sectors = sectorRepository.findAllByUserId(userId);
        return sectorMapper.toDtos(sectors);
    }

    @Transactional
    public List<Sector> findByIds(List<Long> ids) {
        return sectorRepository.findByIds(ids);
    }
}
