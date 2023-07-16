package io.github.janjanda.otava.library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Set;

class ManagerTests {
    @Test
    void oneTableWithDescriptor() throws Exception {
        Manager m = new Manager();
        String[] tables = {"src/test/resources/tables/table004.csv"};
        String[] tableAlias = {"https://example.org/tree-ops.csv"};
        String[] descriptors = {"src/test/resources/metadata/metadata001.json"};
        String[] descAlias = {"https://example.org/metadata001.json"};
        Set<Result> results = m.manualLocalValidation(tables, tableAlias, descriptors, descAlias);
        assertEquals(7, results.size());
    }
}
