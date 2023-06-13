package otava.library.checks;

import otava.library.*;
import otava.library.documents.Table;
import otava.library.results.*;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class checks the line breaks of the tables according to the CSV specification.
 * Line breaks must be CRLF.
 * @see <a href="https://www.rfc-editor.org/rfc/rfc4180.html">RFC4180</a>
 */
public final class LineBreaksCheck extends Check {
    public LineBreaksCheck(CheckFactory f) {
        super(f.getTables(), f.getDescriptors());
    }

    @Override
    protected Result performValidation() throws ValidatorException {
        WarningResult tmpResult = null;
        for (Table table : tables) {
            if (lineBreaksWrong(table)) {
                if (tmpResult == null) tmpResult = new WarningResult();
                tmpResult.addMessage(Manager.locale().badLineSeparators(table.getName()));
            }
        }
        if (tmpResult == null) return new OkResult();
        else return tmpResult;
    }

    private boolean lineBreaksWrong(Table table) throws ValidatorException {
        try (FileReader reader = table.getReader()) {
            boolean afterCR = false;
            int c = reader.read();
            while (c != -1) {
                if (c == '\n' && !afterCR) return true;
                if (afterCR && c != '\n') return true;
                afterCR = c == '\r';
                c = reader.read();
            }
            return false;
        }
        catch (IOException e) {
            throw new ValidatorException(Manager.locale().ioException());
        }
    }
}
