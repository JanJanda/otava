package io.github.janjanda.otava.library.checks;

import static org.junit.jupiter.api.Assertions.*;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.SingletonCheckFactory;
import io.github.janjanda.otava.library.TestUtils;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.documents.DocsGroup;
import io.github.janjanda.otava.library.documents.Table;
import org.junit.jupiter.api.Test;

class RequiredColumnsCheckTests {
    @Test
    void oneRequiredColumn() throws Exception {
        Table[] tables = {TestUtils.createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata001.json", "https://example.org/metadata.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        Result result = rcc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void oneRequiredColumnEmpty() throws Exception {
        Table[] tables = {TestUtils.createTable("table007.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata001.json", "https://example.org/metadata.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        Result result = rcc.validate();
        assertFalse(result.isOk);
        assertTrue(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void moreRequiredColumns() throws Exception {
        Table[] tables = {TestUtils.createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata017.json", "https://example.org/metadata.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        Result result = rcc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void moreRequiredColumnsEmpty() throws Exception {
        Table[] tables = {TestUtils.createTable("table008.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata017.json", "https://example.org/metadata.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        Result result = rcc.validate();
        assertFalse(result.isOk);
        assertTrue(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void checkSkipped() throws Exception {
        Table[] tables = {TestUtils.createTable("table005.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        assertTrue(rcc.validate().isSkipped);
    }

    @Test
    void noRequiredColumns() throws Exception {
        Table[] tables = {TestUtils.createTable("table008.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata018.json", "https://example.org/metadata.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        RequiredColumnsCheck rcc = scf.getInstance(RequiredColumnsCheck.class);
        Result result = rcc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }
}