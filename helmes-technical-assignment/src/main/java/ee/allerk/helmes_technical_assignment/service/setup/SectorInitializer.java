package ee.allerk.helmes_technical_assignment.service.setup;

import ee.allerk.helmes_technical_assignment.dto.seeder.SectorNode;
import ee.allerk.helmes_technical_assignment.model.form.Sector;
import ee.allerk.helmes_technical_assignment.repository.form.SectorRepository;
import ee.allerk.helmes_technical_assignment.utils.SectorTsvParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SectorInitializer {
    private final SectorRepository sectorRepository;

    public void seedSectors() {
        SectorTsvParser parser = new SectorTsvParser();
        List<SectorNode> sectorNodes = parser.parse();

        for (SectorNode sectorNode : sectorNodes) {
            this.saveRecursively(sectorNode, null);
        }
    }

    private void saveRecursively(SectorNode sectorNode, Sector parent) {
        Sector sector = new Sector();
        sector.setLabel(sectorNode.getLabel());
        sector.setParent(parent);
        sectorRepository.save(sector);

        for (SectorNode child : sectorNode.getChildren()) {
            saveRecursively(child, sector);
        }
    }
}
