package io.github.janjanda.otava.library.checks;

import static io.github.janjanda.otava.library.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import io.github.janjanda.otava.library.*;
import io.github.janjanda.otava.library.documents.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DataTypesCheckTests {
    @Test
    void goodDataTypes() throws Exception {
        Table[] tables = {createTable("table009.csv", "https://example.org/tree-ops.csv")};
        Descriptor[] descs = {createDescriptor("metadata019.json", "https://example.org/metadata001.json")};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descs));
        DataTypesCheck dc = scf.getInstance(DataTypesCheck.class);
        Result result = dc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }
}
