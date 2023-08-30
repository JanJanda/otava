package io.github.janjanda.otava.library.checks;

import static io.github.janjanda.otava.library.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import io.github.janjanda.otava.library.*;
import io.github.janjanda.otava.library.documents.*;
import io.github.janjanda.otava.library.factories.SingletonCheckFactory;
import org.junit.jupiter.api.Test;

class ColumnTitlesCheckTests {
    @Test
    void columnsMatch() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ColumnTitlesCheck ctc = scf.getInstance(ColumnTitlesCheck.class);
        Result result = ctc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void missingColumnDescription() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata013.json", "https://example.org/metadata013.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ColumnTitlesCheck ctc = scf.getInstance(ColumnTitlesCheck.class);
        Result result = ctc.validate();
        assertFalse(result.isOk);
        assertTrue(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void missingColumn() throws Exception {
        Table[] tables = {createTable("table005.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ColumnTitlesCheck ctc = scf.getInstance(ColumnTitlesCheck.class);
        assertTrue(ctc.validate().isFatal);
    }

    @Test
    void validationSkipped() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.com/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ColumnTitlesCheck ctc = scf.getInstance(ColumnTitlesCheck.class);
        assertTrue(ctc.validate().isSkipped);
    }

    @Test
    void tableGroup() throws Exception {
        Table[] tables = {createTable("table001.csv", "https://example.org/countries.csv"),
                createTable("table001.csv", "https://example.org/country-groups.csv"),
                createTable("table001.csv", "https://example.org/unemployment.csv")};
        Descriptor[] descs = {createDescriptor("metadata011.json", "https://example.org/metadata011.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ColumnTitlesCheck ctc = scf.getInstance(ColumnTitlesCheck.class);
        assertTrue(ctc.validate().isFatal);
    }
}
