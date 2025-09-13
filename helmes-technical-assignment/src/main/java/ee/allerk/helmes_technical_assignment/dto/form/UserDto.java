package ee.allerk.helmes_technical_assignment.dto.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
public class UserDto {
    private Long id;
    private String name;
    private boolean termsAgreed;
    private Set<Long> sectorIds = new HashSet<>();
}
