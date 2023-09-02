package io.github.janjanda.otava.library.utils;

import io.github.janjanda.otava.library.documents.InMemoryTable;
import org.junit.jupiter.api.Test;

import static io.github.janjanda.otava.library.TestUtils.createTable;
import static io.github.janjanda.otava.library.utils.TableUtils.areValuesInColumns;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TableUtilsTests {
    private InMemoryTable makeTable() throws Exception {
        return createTable("table001.csv", null);
    }

    @Test
    void twoValuesFound() throws Exception {
        String[] values = {"Marge", "Simpson"};
        int[] columns = {0, 1};
        assertTrue(areValuesInColumns(makeTable(), values, columns, 0));
    }

    @Test
    void oneValueFound() throws Exception {
        String[] values = {"Bart"};
        int[] columns = {0};
        assertTrue(areValuesInColumns(makeTable(), values, columns, 0));
    }

    @Test
    void valuesNotFound() throws Exception {
        String[] values = {"Ned", "Simpson"};
        int[] columns = {0, 1};
        assertFalse(areValuesInColumns(makeTable(), values, columns, 0));
    }

    @Test
    void valuesIgnored() throws Exception {
        String[] values = {"Homer", "Simpson"};
        int[] columns = {0, 1};
        assertFalse(areValuesInColumns(makeTable(), values, columns, 2));
    }
}
