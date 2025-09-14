package ee.allerk.helmes_technical_assignment.utils;

import ee.allerk.helmes_technical_assignment.dto.seeder.SectorNode;

import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.Scanner;

public class SectorTsvParser {

    public List<SectorNode> parse() {

        List<SectorNode> roots = new ArrayList<>();
        Deque<SectorNode> stack = new ArrayDeque<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/sectors.tsv")){
            if (inputStream == null) {
                throw new RuntimeException("Sectors file not found!");
            }
            try {
                Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.isBlank()) continue;

                    short depth = this.tabsCounter(line);
                    SectorNode sectorNode = new SectorNode(line.trim());

                    if (depth == 0) {
                        roots.add(sectorNode);
                        stack.clear();
                        stack.push(sectorNode);
                    } else {
                        while (stack.size() > depth) {
                            stack.pop();
                        }
                        SectorNode parent = stack.peek();
                        if (parent != null) {
                            parent.addChild(sectorNode);
                        }
                        stack.push(sectorNode);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error reading sectors file!");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error finding sector file!");
        }

        return roots;
    }

    private short tabsCounter(String line) {
        short count = 0;
        for (char elem : line.toCharArray()) {
            if (elem == '\t') {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
