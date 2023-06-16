package otava.library.checks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import otava.library.*;
import otava.library.documents.*;

class LineBreaksCheckTests {
    private LineBreaksCheck createCheck(String tableName) throws Exception {
        DocumentFactory df = new DocumentFactory();
        LocalInMemoryTable table = df.getLocalTable("src/test/resources/custom-tables/" + tableName);
        DocsGroup<Table> tables = new DocsGroup<>(new Table[]{table});
        SingletonCheckFactory scf = new SingletonCheckFactory(tables, null);
        return scf.getInstance(LineBreaksCheck.class);
    }

    @Test
    void CrLfPass() throws Exception {
        assertTrue(createCheck("table001.csv").validate().isOk());
    }

    @Test
    void LfFail() throws Exception {
        assertFalse(createCheck("table002.csv").validate().isOk());
    }

    @Test
    void CrFail() throws Exception {
        assertFalse(createCheck("table003.csv").validate().isOk());
    }
}
