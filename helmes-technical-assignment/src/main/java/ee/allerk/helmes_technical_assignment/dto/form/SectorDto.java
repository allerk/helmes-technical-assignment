package ee.allerk.helmes_technical_assignment.dto.form;

import ee.allerk.helmes_technical_assignment.model.form.Sector;
import ee.allerk.helmes_technical_assignment.model.form.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
public class SectorDto {
    private Long id;
    private String label;
    private Sector parent;
    private Set<Sector> children = new HashSet<>();
    private Set<User> users = new HashSet<>();
}
