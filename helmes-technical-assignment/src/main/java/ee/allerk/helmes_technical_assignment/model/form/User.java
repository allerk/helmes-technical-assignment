package ee.allerk.helmes_technical_assignment.model.form;

import ee.allerk.helmes_technical_assignment.model.common.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
@Table(name = "users")
public class User extends AbstractEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_terms_agreed")
    private boolean isTermsAgreed;

    @ManyToMany
    @JoinTable(
            name = "users_sectors",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "sector_id") }
    )
    private Set<Sector> sectors = new HashSet<>();
}
