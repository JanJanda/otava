package io.github.janjanda.otava.library.checks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static io.github.janjanda.otava.library.TestUtils.*;
import io.github.janjanda.otava.library.*;
import io.github.janjanda.otava.library.documents.*;

class ForeignKeyCheckTests {
    @Test
    void oneGoodForeignKey() throws Exception {
        Table[] tables = {createTable("table016.csv", "http://example.org/countries.csv"),
            createTable("table017.csv", "http://example.org/country_slice.csv")};
        Descriptor[] descs = {createDescriptor("metadata021.json", "http://example.org/countries.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ForeignKeyCheck fkc = scf.getInstance(ForeignKeyCheck.class);
        Result result = fkc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isSkipped);
        assertFalse(result.isFatal);
    }
}
