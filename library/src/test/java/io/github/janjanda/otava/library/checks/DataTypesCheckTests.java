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

public class DataTypesCheckTests {
    @Test
    void goodDataTypes() throws Exception {
        Table[] tables = {createTable("table009.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata019.json", "https://example.org/metadata019.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        DataTypesCheck dc = scf.getInstance(DataTypesCheck.class);
        Result result = dc.validate();
        assertEquals(Result.State.OK, result.state);
    }

    @Test
    void badDataTypes() throws Exception {
        Table[] tables = {createTable("table010.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata019.json", "https://example.org/metadata019.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        DataTypesCheck dc = scf.getInstance(DataTypesCheck.class);
        Result result = dc.validate();
        assertEquals(Result.State.FATAL, result.state);
        assertEquals(7, result.numberOfMsg);
    }

    @Test
    void goodComplexDataTypes() throws Exception {
        Table[] tables = {createTable("table011.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata020.json", "https://example.org/metadata020.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        DataTypesCheck dc = scf.getInstance(DataTypesCheck.class);
        Result result = dc.validate();
        assertEquals(Result.State.OK, result.state);
    }

    @Test
    void badComplexDataTypes() throws Exception {
        Table[] tables = {createTable("table012.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata020.json", "https://example.org/metadata020.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        DataTypesCheck dc = scf.getInstance(DataTypesCheck.class);
        Result result = dc.validate();
        assertEquals(Result.State.FATAL, result.state);
        assertEquals(6, result.numberOfMsg);
    }
}
