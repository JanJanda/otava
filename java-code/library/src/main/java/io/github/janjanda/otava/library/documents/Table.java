package io.github.janjanda.otava.library.documents;

import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStreamReader;

public interface Table extends Document, Iterable<CSVRecord> {
    CSVRecord getFirstLine() throws ValidatorFileException;
    InputStreamReader getReader() throws ValidatorFileException;
}
