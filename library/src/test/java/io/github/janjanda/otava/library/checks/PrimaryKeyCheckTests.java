package io.github.janjanda.otava.library.checks;

import static org.junit.jupiter.api.Assertions.*;
import io.github.janjanda.otava.library.*;
import static io.github.janjanda.otava.library.TestUtils.*;
import io.github.janjanda.otava.library.documents.*;
import io.github.janjanda.otava.library.factories.SingletonCheckFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PrimaryKeyCheckTests {
    @ParameterizedTest
    @ValueSource(strings = {"metadata001.json", "metadata014.json", "metadata015.json"})
    void primKeys(String fileName) throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor(fileName, "https://example.org/metadata.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        PrimaryKeyCheck pkc = scf.getInstance(PrimaryKeyCheck.class);
        Result result = pkc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void duplicateValuesInRows() throws Exception {
        Table[] tables = {createTable("table006.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata015.json", "https://example.org/metadata015.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        PrimaryKeyCheck pkc = scf.getInstance(PrimaryKeyCheck.class);
        Result result = pkc.validate();
        assertTrue(result.isFatal);
    }

    @Test
    void badMetadataValues() throws Exception {
        Table[] tables = {createTable("table006.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata016.json", "https://example.org/metadata015.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        PrimaryKeyCheck pkc = scf.getInstance(PrimaryKeyCheck.class);
        Result result = pkc.validate();
        assertFalse(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
        assertEquals(2, result.numberOfMsg);
    }

    @Test
    void skipAfterFatal() throws Exception {
        Table[] tables = {createTable("table005.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        PrimaryKeyCheck pkc = scf.getInstance(PrimaryKeyCheck.class);
        Result result = pkc.validate();
        assertTrue(result.isSkipped);
    }
}
