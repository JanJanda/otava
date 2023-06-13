package otava.library.checks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import otava.library.*;
import otava.library.documents.*;

class LineBreaksCheckTests {
    private LineBreaksCheck createCheck(String tableName) throws ValidatorException {
        DocumentFactory df = new DocumentFactory();
        LocalTable table = df.getLocalTable("src/test/resources/custom-tables/" + tableName);
        Documents<Table> tables = new Documents<>(new Table[]{table});
        SingletonCheckFactory scf = new SingletonCheckFactory(tables, null);
        return scf.getInstance(LineBreaksCheck.class);
    }

    @Test
    void CrLfPass() throws ValidatorException {
        assertTrue(createCheck("table001.csv").validate().isOk());
    }

    @Test
    void LfFail() throws ValidatorException {
        assertFalse(createCheck("table002.csv").validate().isOk());
    }

    @Test
    void CrFail() throws ValidatorException {
        assertFalse(createCheck("table003.csv").validate().isOk());
    }
}
