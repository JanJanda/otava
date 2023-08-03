package io.github.janjanda.otava.library.documents;

import org.apache.commons.csv.CSVRecord;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import java.io.InputStreamReader;

public interface Table extends Document, Iterable<CSVRecord> {
    CSVRecord getFirstLine();
    InputStreamReader getReader() throws ValidatorFileException;
}
