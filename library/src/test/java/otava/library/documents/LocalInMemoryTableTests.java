package otava.library.documents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import otava.library.DocumentFactory;

class LocalInMemoryTableTests {
    private LocalInMemoryTable makeTable() throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.getLocalTable("src/test/resources/custom-tables/table001.csv");
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
}
