package io.github.janjanda.otava.library.checks;

import static io.github.janjanda.otava.library.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import io.github.janjanda.otava.library.SingletonCheckFactory;
import io.github.janjanda.otava.library.documents.*;
import org.junit.jupiter.api.Test;

class LineBreaksCheckTests {
    private LineBreaksCheck createCheck(String tableName) throws Exception {
        InMemoryTable table = createTable(tableName, null);
        DocsGroup<Table> tables = new DocsGroup<>(new Table[]{table});
        SingletonCheckFactory scf = new SingletonCheckFactory(tables, null);
        return scf.getInstance(LineBreaksCheck.class);
    }

    @Test
    void CrLfPass() throws Exception {
        assertTrue(createCheck("table001.csv").validate().isOk);
    }

    @Test
    void LfFail() throws Exception {
        assertFalse(createCheck("table002.csv").validate().isOk);
    }

    @Test
    void CrFail() throws Exception {
        assertFalse(createCheck("table003.csv").validate().isOk);
    }
}
