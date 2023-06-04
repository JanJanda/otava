package otava.library.checks;

import otava.library.*;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class checks the line breaks of the tables according to the CSV specification.
 * Line breaks must be CRLF.
 * @see <a href="https://www.rfc-editor.org/rfc/rfc4180.html">RFC4180</a>
 */
final class LineBreaksCheck extends Check {
    public LineBreaksCheck(CheckFactory f) {
        super(f.getTables(), f.getDescriptor());
    }

    @Override
    protected Result performValidation() throws ValidatorException {
        WarningResult tmpResult = null;
        for (Table table : tables) {
            if (lineBreaksWrong(table)) {
                if (tmpResult == null) tmpResult = new WarningResult();
                tmpResult.addMessage("Table " + table.getName() + " has incorrect line separators.");
            }
        }
        if (tmpResult == null) return new OkResult();
        else return tmpResult;
    }

    private boolean lineBreaksWrong(Table table) throws ValidatorException {
        FileReader reader = table.getFileReader();
        try {
            int c = reader.read();
            boolean afterCR = false;
            while (c != -1) {
                if (c == '\n' && !afterCR) return true;
                if (afterCR && c != '\n') return true;
                afterCR = c == '\r';
                c = reader.read();
            }
        }
        catch (IOException e) {
            throw new ValidatorException("Unexpected IOException.");
        }
        return false;
    }
}
