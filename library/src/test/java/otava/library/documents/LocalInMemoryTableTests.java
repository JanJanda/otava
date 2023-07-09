package otava.library.documents;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import otava.library.DocumentFactory;

class LocalInMemoryTableTests {
    private LocalInMemoryTable makeTable() throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.getLocalTable("src/test/resources/tables/table001.csv", null);
    }

    @Test
    void correctWidth() throws Exception {
        assertEquals(2, makeTable().getWidth());
    }

    @Test
    void correctHeight() throws Exception {
        assertEquals(9, makeTable().getHeight());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, Surname",
            "3, 0, Bart",
            "8, 1, Smithers"
    })
    void parsingIntoCellsTest(int row, int column, String value) throws Exception {
        assertEquals(value, makeTable().getCell(row, column));
    }

    @Test
    void twoValuesFound() throws Exception {
        String[] values = {"Marge", "Simpson"};
        int[] columns = {0, 1};
        assertTrue(makeTable().areValuesInColumns(values, columns, 0));
    }

    @Test
    void oneValueFound() throws Exception {
        String[] values = {"Bart"};
        int[] columns = {0};
        assertTrue(makeTable().areValuesInColumns(values, columns, 0));
    }

    @Test
    void valuesNotFound() throws Exception {
        String[] values = {"Ned", "Simpson"};
        int[] columns = {0, 1};
        assertFalse(makeTable().areValuesInColumns(values, columns, 0));
    }

    @Test
    void valuesIgnored() throws Exception {
        String[] values = {"Homer", "Simpson"};
        int[] columns = {0, 1};
        assertFalse(makeTable().areValuesInColumns(values, columns, 2));
    }
}
