package otava.library.checks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import otava.library.*;
import otava.library.documents.*;

class RequiredColumnsCheckTests {
    private LocalInMemoryTable createTable(String name, String alias) throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.getLocalTable("src/test/resources/tables/" + name, alias);
    }

    private LocalDescriptor createDescriptor(String name, String alias) throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.getLocalDescriptor("src/test/resources/metadata/" + name, alias);
    }

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
