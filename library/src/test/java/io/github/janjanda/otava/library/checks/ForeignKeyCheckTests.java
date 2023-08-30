package io.github.janjanda.otava.library.checks;

import static org.junit.jupiter.api.Assertions.*;

import io.github.janjanda.otava.library.factories.SingletonCheckFactory;
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

    @Test
    void oneBadForeignKey() throws Exception {
        Table[] tables = {createTable("table016.csv", "http://example.org/countries.csv"),
                createTable("table018.csv", "http://example.org/country_slice.csv")};
        Descriptor[] descs = {createDescriptor("metadata021.json", "http://example.org/countries.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ForeignKeyCheck fkc = scf.getInstance(ForeignKeyCheck.class);
        Result result = fkc.validate();
        assertFalse(result.isOk);
        assertFalse(result.isSkipped);
        assertTrue(result.isFatal);
    }

    @Test
    void multiColumnGoodForeignKey() throws Exception {
        Table[] tables = {createTable("table004.csv", "http://example.org/tree-ops.csv"),
                createTable("table019.csv", "http://example.org/reference.csv")};
        Descriptor[] descs = {createDescriptor("metadata022.json", "http://example.org/metadata022.json"),
                createDescriptor("metadata023.json", "http://example.org/metadata023.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ForeignKeyCheck fkc = scf.getInstance(ForeignKeyCheck.class);
        Result result = fkc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isSkipped);
        assertFalse(result.isFatal);
    }

    @Test
    void multiColumnBadForeignKey() throws Exception {
        Table[] tables = {createTable("table020.csv", "http://example.org/tree-ops.csv"),
                createTable("table019.csv", "http://example.org/reference.csv")};
        Descriptor[] descs = {createDescriptor("metadata022.json", "http://example.org/metadata022.json"),
                createDescriptor("metadata023.json", "http://example.org/metadata023.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ForeignKeyCheck fkc = scf.getInstance(ForeignKeyCheck.class);
        Result result = fkc.validate();
        assertFalse(result.isOk);
        assertFalse(result.isSkipped);
        assertTrue(result.isFatal);
    }
}
