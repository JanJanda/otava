package io.github.janjanda.otava.library.documents;

import org.apache.commons.csv.CSVRecord;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import java.io.InputStreamReader;

public interface Table extends Document, Iterable<CSVRecord> {
    int getWidth();
    int getHeight();
    String getCell(int row, int column);
    CSVRecord getFirstLine();
    InputStreamReader getReader() throws ValidatorFileException;
}
