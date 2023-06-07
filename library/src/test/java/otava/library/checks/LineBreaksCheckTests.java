package otava.library.checks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import otava.library.*;

class LineBreaksCheckTests {
    private LineBreaksCheck createCheck(String tableName) throws ValidatorException {
        DocumentFactory df = new DocumentFactory();
        LocalTable table = df.getLocalTable("src/test/resources/custom-tables/" + tableName);
        Tables tables = new Tables(table);
        SingletonCheckFactory scf = new SingletonCheckFactory(tables, null);
        return scf.getInstance(LineBreaksCheck.class);
    }

    @Test
    void CrLfPass() throws ValidatorException {
        LineBreaksCheck lbc = createCheck("table001.csv");
        Result result = lbc.validate();
        assertTrue(result.isOk());
    }

    @Test
    void LfFail() throws ValidatorException {
        LineBreaksCheck lbc = createCheck("table002.csv");
        Result result = lbc.validate();
        assertFalse(result.isOk());
    }
}
