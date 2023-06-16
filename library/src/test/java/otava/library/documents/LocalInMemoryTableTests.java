package otava.library.documents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import otava.library.DocumentFactory;

class LocalInMemoryTableTests {
    @Test
    void correctWidth() throws Exception {
        DocumentFactory df = new DocumentFactory();
        LocalInMemoryTable table = df.getLocalTable("src/test/resources/custom-tables/table001.csv");
        assertEquals(2, table.getWidth());
    }

    @Test
    void correctHeight() throws Exception {
        DocumentFactory df = new DocumentFactory();
        LocalInMemoryTable table = df.getLocalTable("src/test/resources/custom-tables/table001.csv");
        assertEquals(9, table.getHeight());

    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, Surname",
            "3, 0, Bart",
            "8, 1, Smithers"
    })
    void parsingIntoCellsTest(int row, int column, String value) throws Exception {
        DocumentFactory df = new DocumentFactory();
        LocalInMemoryTable table = df.getLocalTable("src/test/resources/custom-tables/table001.csv");
        assertEquals(value, table.getCell(row, column));
    }
}
