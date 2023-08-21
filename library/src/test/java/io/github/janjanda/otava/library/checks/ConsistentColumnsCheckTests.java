package io.github.janjanda.otava.library.checks;

import static io.github.janjanda.otava.library.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import io.github.janjanda.otava.library.*;
import io.github.janjanda.otava.library.documents.*;
import io.github.janjanda.otava.library.factories.SingletonCheckFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ConsistentColumnsCheckTests {
    @Test
    void correctRows() throws Exception {
        Table[] tables = {createTable("table001.csv", null)};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), null);
        ConsistentColumnsCheck ccc = scf.getInstance(ConsistentColumnsCheck.class);
        Result result = ccc.validate();
        assertTrue(result.isOk);
        assertFalse(result.isFatal);
        assertFalse(result.isSkipped);
    }

    @ParameterizedTest
    @ValueSource(strings = {"table013.csv", "table014.csv", "table015.csv"})
    void incorrectRows(String tableName) throws Exception {
        Table[] tables = {createTable(tableName, null)};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), null);
        ConsistentColumnsCheck ccc = scf.getInstance(ConsistentColumnsCheck.class);
        Result result = ccc.validate();
        assertFalse(result.isOk);
        assertTrue(result.isFatal);
        assertFalse(result.isSkipped);
    }
}
