package ee.allerk.helmes_technical_assignment.model.form;

import ee.allerk.helmes_technical_assignment.model.common.AbstractEntity;
import jakarta.persistence.*;

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
@Table(name = "sectors")
public class Sector extends AbstractEntity {

    @Column(name = "label", nullable = false)
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Sector parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Sector> children = new HashSet<>();

    @ManyToMany(mappedBy = "sectors")
    private Set<User> users = new HashSet<>();
}
