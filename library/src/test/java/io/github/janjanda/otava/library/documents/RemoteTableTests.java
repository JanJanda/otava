package io.github.janjanda.otava.library.documents;

import static org.junit.jupiter.api.Assertions.*;
import io.github.janjanda.otava.library.factories.DocumentFactory;
import io.github.janjanda.otava.library.exceptions.*;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

class RemoteTableTests {
    private RemoteTable createRemoteTable(String path) {
        DocumentFactory df = new DocumentFactory();
        return df.makeLocalRemoteTable("src/test/resources/tables/" + path, null);
    }

    @Test
    void emptyTableFirstLine() throws Exception {
        RemoteTable table = createRemoteTable("table015.csv");
        assertNull(table.getFirstLine());
    }

    @Test
    void readFirstLineTwice() throws Exception {
        RemoteTable table = createRemoteTable("table001.csv");
        CSVRecord fl1 = table.getFirstLine();
        CSVRecord fl2 = table.getFirstLine();
        assertEquals("Surname", fl1.get(0));
        assertEquals("Surname", fl2.get(0));
    }

    @Test
    void firstLineMissingTable() {
        RemoteTable table = createRemoteTable("noTable");
        assertThrowsExactly(ValidatorFileException.class, table::getFirstLine);
    }

    @Test
    void iterateMissingTable() {
        RemoteTable table = createRemoteTable("noTable");
        assertThrowsExactly(FileIteratorException.class, () -> {for (CSVRecord row : table){}});
    }

    @Test
    void iterateEmptyTable() {
        RemoteTable table = createRemoteTable("table015.csv");
        int i = 0;
        for (CSVRecord row : table) {
            i++;
        }
        assertEquals(0, i);
    }

    @Test
    void iterateTable() {
        RemoteTable table = createRemoteTable("table001.csv");
        int i = 0;
        for (CSVRecord row : table) {
            i++;
        }
        assertEquals(9, i);
    }
}
