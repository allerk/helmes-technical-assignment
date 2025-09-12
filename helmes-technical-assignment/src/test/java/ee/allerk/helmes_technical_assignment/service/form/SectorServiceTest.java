package ee.allerk.helmes_technical_assignment.service.form;

import ee.allerk.helmes_technical_assignment.dto.form.SectorDto;
import ee.allerk.helmes_technical_assignment.mapper.form.SectorMapper;
import ee.allerk.helmes_technical_assignment.mapper.form.SectorMapperImpl;
import ee.allerk.helmes_technical_assignment.model.form.Sector;
import ee.allerk.helmes_technical_assignment.repository.form.SectorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class SectorServiceTest {

    @Spy
    private SectorMapper sectorMapper = new SectorMapperImpl();

    @Mock
    private SectorRepository sectorRepository;

    @InjectMocks
    private SectorService sectorService;

    @Test
    @DisplayName("Find all sectors including parent and child entities to display")
    void findAllSectorsIncludingParentAndChild() {
        List<Sector> sectors = new ArrayList<>();

        Sector mockSector1Deep = Sector.builder()
                .id(1L)
                .label("Manufacturing")
                .build();
        Sector mockSector2Deep1 = Sector.builder()
                .id(2L)
                .label("Construction materials")
                .build();
        mockSector2Deep1.setParent(mockSector1Deep);
        Sector mockSector2Deep2 = Sector.builder()
                .id(3L)
                .label("Food and Beverage")
                .build();
        mockSector2Deep2.setParent(mockSector1Deep);
        Sector mockSector3Deep1 = Sector.builder()
                .id(4L)
                .label("Bakery & confectionery products")
                .build();
        mockSector3Deep1.setParent(mockSector2Deep2);
        Sector mockSector1Deep2 = Sector.builder()
                .id(5L)
                .label("Other")
                .build();

        Set<Sector> mockSector2Deep2Childs = new HashSet<>();
        mockSector2Deep2Childs.add(mockSector3Deep1);
        mockSector2Deep2.setChildren(mockSector2Deep2Childs);

        Set<Sector> mockSector1DeepChilds = new HashSet<>();
        mockSector1DeepChilds.add(mockSector2Deep1);
        mockSector1DeepChilds.add(mockSector2Deep2);
        mockSector1Deep.setChildren(mockSector1DeepChilds);

        sectors.add(mockSector1Deep);
        sectors.add(mockSector2Deep1);
        sectors.add(mockSector2Deep2);
        sectors.add(mockSector3Deep1);
        sectors.add(mockSector1Deep2);

        List<SectorDto> sectorDtos = sectorMapper.toDtos(sectors);
        when(sectorRepository.findAll()).thenReturn(sectors);
        when(sectorMapper.toDtos(anyList())).thenReturn(sectorDtos);

        List<SectorDto> results = sectorService.findAll();

        verify(sectorRepository, times(1)).findAll();

        // returned list is the same as expected list
        assertEquals(sectorDtos, results);
        // compare parent foreign keys
        assertEquals(1L, mockSector2Deep1.getParent().getId());
        assertEquals(1L, mockSector2Deep2.getParent().getId());
        assertEquals(3L, mockSector3Deep1.getParent().getId());
        // compare child foreign keys
        for (Sector child : mockSector1Deep.getChildren()) {
            assertEquals(1L, child.getParent().getId());
        }
        for (Sector child : mockSector2Deep2.getChildren()) {
            assertEquals(3L, child.getParent().getId());
        }
    }

    @Test
    @DisplayName("Find User Sectors")
    void findUserSectors() {
        Long userId = 1L;

        Sector relatedSector = Sector.builder()
                .id(1L)
                .label("Manufacturing")
                .build();
        Sector notRelatedSector = Sector.builder()
                .id(2L)
                .label("Construction materials")
                .build();

        List<Sector> sectors = List.of(relatedSector);
        List<SectorDto> expectedDtos = List.of(
                sectorMapper.toDto(relatedSector)
        );

        when(sectorRepository.findAllByUserId(userId)).thenReturn(sectors);
        when(sectorMapper.toDtos(anyList())).thenReturn(expectedDtos);

        List<SectorDto> results = sectorService.findUserSectors(userId);

        verify(sectorRepository, times(1)).findAllByUserId(userId);
        verify(sectorMapper, times(1)).toDtos(sectors);

        assertNotNull(results);
        assertEquals(expectedDtos.size(), results.size());
        assertEquals("Manufacturing", results.get(0).getLabel());

        assertTrue(results.stream().noneMatch(s -> s.getLabel().equals(notRelatedSector.getLabel())));
    }

    @Test
    @DisplayName("Find by list of ids")
    void findByListOfIds() {
        List<Long> ids = List.of(1L, 2L, 3L);
        Sector sector1 = Sector.builder()
                .id(1L)
                .label("Manufacturing")
                .build();
        Sector sector2 = Sector.builder()
                .id(2L)
                .label("Construction materials")
                .build();
        Sector sector3 = Sector.builder()
                .id(2L)
                .label("Other")
                .build();

        List<Sector> expectedResult = List.of(sector1, sector2, sector3);

        when(sectorRepository.findByIds(ids)).thenReturn(expectedResult);

        List<Sector> results = sectorService.findByIds(ids);

        verify(sectorRepository, times(1)).findByIds(ids);
        assertNotNull(results);
        assertEquals(expectedResult.size(), results.size());
        assertEquals("Construction materials", results.get(1).getLabel());
    }
}
