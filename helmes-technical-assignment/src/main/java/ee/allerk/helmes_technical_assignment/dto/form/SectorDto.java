package ee.allerk.helmes_technical_assignment.dto.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
public class SectorDto {
    private Long id;
    private String label;
    private Set<SectorDto> children = new LinkedHashSet<>();
}
