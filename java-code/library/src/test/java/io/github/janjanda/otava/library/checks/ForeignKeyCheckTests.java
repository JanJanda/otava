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

class ForeignKeyCheckTests {
    @Test
    void oneGoodForeignKey() throws Exception {
        Table[] tables = {createTable("table016.csv", "http://example.org/countries.csv"),
            createTable("table017.csv", "http://example.org/country_slice.csv")};
        Descriptor[] descs = {createDescriptor("metadata021.json", "http://example.org/countries.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ForeignKeyCheck fkc = scf.getInstance(ForeignKeyCheck.class);
        Result result = fkc.validate();
        assertEquals(Result.State.OK, result.state);
    }

    @Test
    void oneBadForeignKey() throws Exception {
        Table[] tables = {createTable("table016.csv", "http://example.org/countries.csv"),
                createTable("table018.csv", "http://example.org/country_slice.csv")};
        Descriptor[] descs = {createDescriptor("metadata021.json", "http://example.org/countries.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        ForeignKeyCheck fkc = scf.getInstance(ForeignKeyCheck.class);
        Result result = fkc.validate();
        assertEquals(Result.State.FATAL, result.state);
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
        assertEquals(Result.State.OK, result.state);
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
        assertEquals(Result.State.FATAL, result.state);
    }
}
