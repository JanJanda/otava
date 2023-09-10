package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import io.github.janjanda.otava.library.factories.CheckFactory;

import java.io.IOException;
import java.io.InputStreamReader;

import static io.github.janjanda.otava.library.Manager.locale;

/**
 * This class checks the line breaks of the tables according to the CSV specification.
 * Line breaks must be CRLF.
 * The check does not create fatal results because this requirement is not universally accepted.
 * @see <a href="https://www.rfc-editor.org/rfc/rfc4180.html">RFC4180</a>
 */
public final class LineBreaksCheck extends Check {
    public LineBreaksCheck(CheckFactory f) {
        super(f.getTables(), f.getDescriptors());
    }

    @Override
    protected Result.Builder performValidation() throws ValidatorFileException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        for (Table table : tables) {
            if (lineBreaksWrong(table)) resultBuilder.addMessage(locale().badLineSeparators(table.getName()));
        }
        return resultBuilder;
    }

    private boolean lineBreaksWrong(Table table) throws ValidatorFileException {
        try (InputStreamReader reader = table.getReader()) {
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
            throw new ValidatorFileException(locale().ioException(table.getName()) + " --- " + e.getMessage());
        }
    }
}
