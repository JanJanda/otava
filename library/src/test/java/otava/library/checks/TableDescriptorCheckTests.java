package otava.library.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import otava.library.*;
import otava.library.documents.*;

class TableDescriptorCheckTests {
    private LocalInMemoryTable createTable(String name, String alias) throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.getLocalTable("src/test/resources/tables/" + name, alias);
    }

    private LocalDescriptor createDescriptor(String name) throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.getLocalDescriptor("src/test/resources/metadata/" + name, null);
    }

    @Test
    void tableWithDescriptor() throws Exception {
        Table[] tables = {createTable("table004.csv", "tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isOk);
    }

    @Test
    void tableWithWrongAlias() throws Exception {
        Table[] tables = {createTable("table004.csv", "tree-opsx.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isFatal);
    }

    @Test
    void tableWithNoAlias() throws Exception {
        Table[] tables = {createTable("table004.csv", null)};
        Descriptor[] descs = {createDescriptor("metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isFatal);
    }

    @Test
    void missingTable() throws Exception {
        Table[] tables = new Table[0];
        Descriptor[] descs = {createDescriptor("metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isFatal);
    }

    @Test
    void missingDescriptor() throws Exception {
        Table[] tables = {createTable("table004.csv", "tree-ops.csv")};
        Descriptor[] descs = new Descriptor[0];
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isFatal);
    }

    @Test
    void tableGroup() throws Exception {
        Table[] tables = {createTable("table001.csv", "countries.csv"),
                createTable("table001.csv", "country-groups.csv"),
                createTable("table001.csv", "unemployment.csv")};
        Descriptor[] descs = {createDescriptor("metadata011.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isOk);
    }

    @Test
    void tableGroupWrongTable() throws Exception {
        Table[] tables = {createTable("table001.csv", "countries.csv"),
                createTable("table001.csv", "country-groups.csv"),
                createTable("table001.csv", "unemploymentx.csv")};
        Descriptor[] descs = {createDescriptor("metadata011.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isFatal);
    }

    @Test
    void badContextSkip() throws Exception {
        Table[] tables = {createTable("table004.csv", "tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata005.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isSkipped);
    }
}
