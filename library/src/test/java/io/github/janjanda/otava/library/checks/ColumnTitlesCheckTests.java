package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.documents.DocsGroup;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.factories.SingletonCheckFactory;
import org.junit.jupiter.api.Test;

import static io.github.janjanda.otava.library.TestUtils.createDescriptor;
import static io.github.janjanda.otava.library.TestUtils.createTable;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ColumnTitlesCheckTests {
    @Test
    void columnsMatch() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ColumnTitlesCheck ctc = scf.getInstance(ColumnTitlesCheck.class);
        Result result = ctc.validate();
        assertEquals(Result.State.OK, result.state);
    }

    @Test
    void missingColumnDescription() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata013.json", "https://example.org/metadata013.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ColumnTitlesCheck ctc = scf.getInstance(ColumnTitlesCheck.class);
        Result result = ctc.validate();
        assertEquals(Result.State.FATAL, result.state);
    }

    @Test
    void missingColumn() throws Exception {
        Table[] tables = {createTable("table005.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ColumnTitlesCheck ctc = scf.getInstance(ColumnTitlesCheck.class);
        assertEquals(Result.State.FATAL, ctc.validate().state);
    }

    @Test
    void validationSkipped() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.com/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ColumnTitlesCheck ctc = scf.getInstance(ColumnTitlesCheck.class);
        assertEquals(Result.State.SKIPPED, ctc.validate().state);
    }

    @Test
    void tableGroup() throws Exception {
        Table[] tables = {createTable("table001.csv", "https://example.org/countries.csv"),
                createTable("table001.csv", "https://example.org/country-groups.csv"),
                createTable("table001.csv", "https://example.org/unemployment.csv")};
        Descriptor[] descs = {createDescriptor("metadata011.json", "https://example.org/metadata011.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ColumnTitlesCheck ctc = scf.getInstance(ColumnTitlesCheck.class);
        assertEquals(Result.State.FATAL, ctc.validate().state);
    }
}
