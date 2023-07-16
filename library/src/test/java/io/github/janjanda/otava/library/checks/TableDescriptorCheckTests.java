package io.github.janjanda.otava.library.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;
import io.github.janjanda.otava.library.SingletonCheckFactory;
import io.github.janjanda.otava.library.TestUtils;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.documents.DocsGroup;
import io.github.janjanda.otava.library.documents.Table;
import org.junit.jupiter.api.Test;

class TableDescriptorCheckTests {
    @Test
    void tableWithDescriptor() throws Exception {
        Table[] tables = {TestUtils.createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isOk);
    }

    @Test
    void malformedUrl() throws Exception {
        Table[] tables = {TestUtils.createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata001.json", "asdfjkl")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isFatal);
    }

    @Test
    void tableWithBaseDescriptor() throws Exception {
        Table[] tables = {TestUtils.createTable("table004.csv", "https://example.com/tree-ops.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata012.json", "https://example.org/metadata012.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isOk);
    }

    @Test
    void tableWithWrongAlias() throws Exception {
        Table[] tables = {TestUtils.createTable("table004.csv", "https://example.org/tree-opsx.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isFatal);
    }

    @Test
    void tableWithNoAlias() throws Exception {
        Table[] tables = {TestUtils.createTable("table004.csv", null)};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isFatal);
    }

    @Test
    void missingTable() throws Exception {
        Table[] tables = new Table[0];
        Descriptor[] descs = {TestUtils.createDescriptor("metadata001.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isFatal);
    }

    @Test
    void missingDescriptor() throws Exception {
        Table[] tables = {TestUtils.createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = new Descriptor[0];
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isFatal);
    }

    @Test
    void tableGroup() throws Exception {
        Table[] tables = {TestUtils.createTable("table001.csv", "https://example.org/countries.csv"),
                TestUtils.createTable("table001.csv", "https://example.org/country-groups.csv"),
                TestUtils.createTable("table001.csv", "https://example.org/unemployment.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata011.json", "https://example.org/metadata011.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isOk);
    }

    @Test
    void tableGroupWrongTable() throws Exception {
        Table[] tables = {TestUtils.createTable("table001.csv", "https://example.org/countries.csv"),
                TestUtils.createTable("table001.csv", "https://example.org/country-groups.csv"),
                TestUtils.createTable("table001.csv", "https://example.org/unemploymentx.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata011.json", "https://example.org/metadata011.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isFatal);
    }

    @Test
    void badContextSkip() throws Exception {
        Table[] tables = {TestUtils.createTable("table004.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {TestUtils.createDescriptor("metadata005.json", "https://example.org/metadata005.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        TableDescriptorCheck tdc = scf.getInstance(TableDescriptorCheck.class);
        assertTrue(tdc.validate().isSkipped);
    }
}
