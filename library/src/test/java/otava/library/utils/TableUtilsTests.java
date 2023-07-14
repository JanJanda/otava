package otava.library.utils;

import static otava.library.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static otava.library.utils.TableUtils.*;
import org.junit.jupiter.api.Test;
import otava.library.documents.LocalInMemoryTable;

class TableUtilsTests {
    private LocalInMemoryTable makeTable() throws Exception {
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
