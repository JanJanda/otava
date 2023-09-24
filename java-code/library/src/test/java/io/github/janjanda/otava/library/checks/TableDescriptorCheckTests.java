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

class TableDescriptorCheckTests {
    @Test
    void tableWithDescriptor() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        Result result = tdc.validate();
        assertEquals(Result.State.OK, result.state);
    }

    @Test
    void malformedUrl() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "asdfjkl")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertEquals(Result.State.FATAL, tdc.validate().state);
    }

    @Test
    void tableWithBaseDescriptor() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.com/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata012.json", "https://example.org/metadata012.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        Result result = tdc.validate();
        assertEquals(Result.State.OK, result.state);
    }

    @Test
    void tableWithWrongAlias() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-opsx.csv")};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertEquals(Result.State.FATAL, tdc.validate().state);
    }

    @Test
    void tableWithNoAlias() throws Exception {
        Table[] tables = {createTable("table004.csv", null)};
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertEquals(Result.State.FATAL, tdc.validate().state);
    }

    @Test
    void missingTable() throws Exception {
        Table[] tables = new Table[0];
        Descriptor[] descs = {createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertEquals(Result.State.FATAL, tdc.validate().state);
    }

    @Test
    void missingDescriptor() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = new Descriptor[0];
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertEquals(Result.State.FATAL, tdc.validate().state);
    }

    @Test
    void tableGroup() throws Exception {
        Table[] tables = {createTable("table001.csv", "https://example.org/countries.csv"),
                createTable("table001.csv", "https://example.org/country-groups.csv"),
                createTable("table001.csv", "https://example.org/unemployment.csv")};
        Descriptor[] descs = {createDescriptor("metadata011.json", "https://example.org/metadata011.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        Result result = tdc.validate();
        assertEquals(Result.State.OK, result.state);
    }

    @Test
    void tableGroupWrongTable() throws Exception {
        Table[] tables = {createTable("table001.csv", "https://example.org/countries.csv"),
                createTable("table001.csv", "https://example.org/country-groups.csv"),
                createTable("table001.csv", "https://example.org/unemploymentx.csv")};
        Descriptor[] descs = {createDescriptor("metadata011.json", "https://example.org/metadata011.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertEquals(Result.State.FATAL, tdc.validate().state);
    }

    @Test
    void badContextSkip() throws Exception {
        Table[] tables = {createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata005.json", "https://example.org/metadata005.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertEquals(Result.State.SKIPPED, tdc.validate().state);
    }
}
