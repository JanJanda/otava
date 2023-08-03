package io.github.janjanda.otava.library.documents;

import static org.junit.jupiter.api.Assertions.*;
import io.github.janjanda.otava.library.DocumentFactory;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

class RemoteTableTests {
    private RemoteTable createRemoteTable(String path) throws Exception {
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
/*
    @Test
    void iterateMissingTable() throws Exception {
        RemoteTable table = createRemoteTable("noTable");
        for (CSVRecord row : table) {

        }
    }*/
}
