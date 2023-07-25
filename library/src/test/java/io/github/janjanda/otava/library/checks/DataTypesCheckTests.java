package io.github.janjanda.otava.library.checks;

import static io.github.janjanda.otava.library.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import io.github.janjanda.otava.library.*;
import io.github.janjanda.otava.library.documents.*;
import org.junit.jupiter.api.Test;

public class DataTypesCheckTests {
    @Test
    void goodDataTypes() throws Exception {
        Table[] tables = {createTable("table009.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata019.json", "https://example.org/metadata019.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        DataTypesCheck dc = scf.getInstance(DataTypesCheck.class);
        Result result = dc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void badDataTypes() throws Exception {
        Table[] tables = {createTable("table010.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata019.json", "https://example.org/metadata019.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        DataTypesCheck dc = scf.getInstance(DataTypesCheck.class);
        Result result = dc.validate();
        assertFalse(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
        assertEquals(7, result.numberOfMsg);
    }

    @Test
    void goodComplexDataTypes() throws Exception {
        Table[] tables = {createTable("table011.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata020.json", "https://example.org/metadata020.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        DataTypesCheck dc = scf.getInstance(DataTypesCheck.class);
        Result result = dc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @Test
    void badComplexDataTypes() throws Exception {
        Table[] tables = {createTable("table012.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata020.json", "https://example.org/metadata020.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        DataTypesCheck dc = scf.getInstance(DataTypesCheck.class);
        Result result = dc.validate();
        assertFalse(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
        assertEquals(6, result.numberOfMsg);
    }
}
