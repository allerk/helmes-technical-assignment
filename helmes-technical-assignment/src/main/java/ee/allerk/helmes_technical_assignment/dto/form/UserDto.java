package ee.allerk.helmes_technical_assignment.dto.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
public class UserDto {
    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @AssertTrue(message = "You must agree to terms")
    private Boolean termsAgreed;
    @NotEmpty(message = "At least one sector must be selected")
    private Set<Long> sectorIds = new HashSet<>();
}
