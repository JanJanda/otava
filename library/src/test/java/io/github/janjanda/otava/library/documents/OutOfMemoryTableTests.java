package io.github.janjanda.otava.library.documents;

import io.github.janjanda.otava.library.exceptions.FileIteratorException;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import io.github.janjanda.otava.library.factories.DocumentFactory;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OutOfMemoryTableTests {
    private OutOfMemoryTable createOutOfMemoryTable(String path) {
        DocumentFactory df = new DocumentFactory();
        return df.makeLocalOutOfMemoryTable("src/test/resources/tables/" + path, null);
    }

    @Test
    void emptyTableFirstLine() throws Exception {
        OutOfMemoryTable table = createOutOfMemoryTable("table015.csv");
        assertNull(table.getFirstLine());
    }

    @Test
    void readFirstLineTwice() throws Exception {
        OutOfMemoryTable table = createOutOfMemoryTable("table001.csv");
        CSVRecord fl1 = table.getFirstLine();
        CSVRecord fl2 = table.getFirstLine();
        assertEquals("Surname", fl1.get(0));
        assertEquals("Surname", fl2.get(0));
    }

    @Test
    void firstLineMissingTable() {
        OutOfMemoryTable table = createOutOfMemoryTable("noTable");
        assertThrowsExactly(ValidatorFileException.class, table::getFirstLine);
    }

    @Test
    void iterateMissingTable() {
        OutOfMemoryTable table = createOutOfMemoryTable("noTable");
        assertThrowsExactly(FileIteratorException.class, () -> {for (CSVRecord row : table){}});
    }

    @Test
    void iterateEmptyTable() {
        OutOfMemoryTable table = createOutOfMemoryTable("table015.csv");
        int i = 0;
        for (CSVRecord row : table) {
            i++;
        }
        assertEquals(0, i);
    }

    @Test
    void iterateTable() {
        OutOfMemoryTable table = createOutOfMemoryTable("table001.csv");
        int i = 0;
        for (CSVRecord row : table) {
            i++;
        }
        assertEquals(9, i);
    }
}
