package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.DocsGroup;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.factories.SingletonCheckFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.github.janjanda.otava.library.TestUtils.createTable;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsistentColumnsCheckTests {
    @Test
    void correctRows() throws Exception {
        Table[] tables = {createTable("table001.csv", null)};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), null);
        ConsistentColumnsCheck ccc = scf.getInstance(ConsistentColumnsCheck.class);
        Result result = ccc.validate();
        assertEquals(Result.State.OK, result.state);
    }

    @ParameterizedTest
    @ValueSource(strings = {"table013.csv", "table014.csv", "table015.csv"})
    void incorrectRows(String tableName) throws Exception {
        Table[] tables = {createTable(tableName, null)};
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), null);
        ConsistentColumnsCheck ccc = scf.getInstance(ConsistentColumnsCheck.class);
        Result result = ccc.validate();
        assertEquals(Result.State.FATAL, result.state);
    }
}
