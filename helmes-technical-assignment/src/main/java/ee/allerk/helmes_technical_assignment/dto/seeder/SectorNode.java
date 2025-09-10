package ee.allerk.helmes_technical_assignment.dto.seeder;

import java.util.ArrayList;
import java.util.List;

public class SectorNode {
    private String label;
    private List<SectorNode> children = new ArrayList<>();

    public SectorNode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public List<SectorNode> getChildren() {
        return children;
    }

    public void addChild(SectorNode child) {
        children.add(child);
    }
}
