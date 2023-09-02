package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.documents.DocsGroup;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.factories.SingletonCheckFactory;
import org.junit.jupiter.api.Test;

import static io.github.janjanda.otava.library.TestUtils.createDescriptor;
import static io.github.janjanda.otava.library.TestUtils.createTable;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequiredColumnsCheckTests {
    @Test
    void oneRequiredColumn() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        Result result = rcc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void oneRequiredColumnEmpty() throws Exception {
        Table[] tables = {createTable("table007.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        Result result = rcc.validate();
        assertFalse(result.isOk);
        assertTrue(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void moreRequiredColumns() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata017.json", "https://example.org/metadata.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        Result result = rcc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void moreRequiredColumnsEmpty() throws Exception {
        Table[] tables = {createTable("table008.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata017.json", "https://example.org/metadata.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        Result result = rcc.validate();
        assertFalse(result.isOk);
        assertTrue(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void checkSkipped() throws Exception {
        Table[] tables = {createTable("table005.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        assertTrue(rcc.validate().isSkipped);
    }

    @Test
    void noRequiredColumns() throws Exception {
        Table[] tables = {createTable("table008.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata018.json", "https://example.org/metadata.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        Result result = rcc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }
}
