package ee.allerk.helmes_technical_assignment.service.setup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class Initializer {

    private final SectorInitializer sectorInitializer;

    /**
     * This assumes that:
     * Database structure has been created already
     */
    public void setupDatabase() {
        log.info("setupDatabase Started. This might take some time, please wait....");

        sectorInitializer.seedSectors();

        log.info("setupDatabase Ended");
    }
}
