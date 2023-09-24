package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.DocsGroup;
import io.github.janjanda.otava.library.documents.InMemoryTable;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.factories.SingletonCheckFactory;
import org.junit.jupiter.api.Test;

import static io.github.janjanda.otava.library.TestUtils.createTable;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LineBreaksCheckTests {
    private LineBreaksCheck createCheck(String tableName) throws Exception {
        InMemoryTable table = createTable(tableName, null);
        DocsGroup<Table> tables = new DocsGroup<>(new Table[]{table});
        SingletonCheckFactory scf = new SingletonCheckFactory(tables, null);
        return scf.getInstance(LineBreaksCheck.class);
    }

    @Test
    void CrLfPass() throws Exception {
        assertEquals(Result.State.OK, createCheck("table001.csv").validate().state);
    }

    @Test
    void LfFail() throws Exception {
        assertEquals(Result.State.WARN, createCheck("table002.csv").validate().state);
    }

    @Test
    void CrFail() throws Exception {
        assertEquals(Result.State.WARN, createCheck("table003.csv").validate().state);
    }
}
