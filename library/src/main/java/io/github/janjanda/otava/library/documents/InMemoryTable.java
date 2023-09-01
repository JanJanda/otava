package io.github.janjanda.otava.library.documents;

import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import static io.github.janjanda.otava.library.Manager.locale;

public final class InMemoryTable implements Table {
    private final String fileName;
    private final String alias;
    private final ReaderMaker readerMaker;
    private List<CSVRecord> csvRecords;

    public InMemoryTable(String fileName, String alias, CSVFormat csvFormat, ReaderMaker readerMaker) throws ValidatorFileException {
        this.fileName = fileName;
        this.alias = alias;
        this.readerMaker = readerMaker;
        fillCells(csvFormat);
    }

    private void fillCells(CSVFormat csvFormat) throws ValidatorFileException {
        try (InputStreamReader reader = readerMaker.makeReader(fileName);
             CSVParser csvParser = CSVParser.parse(reader, csvFormat)) {
            csvRecords = csvParser.getRecords();
        }
        catch (IOException e) {
            throw new ValidatorFileException(locale().ioException(fileName));
        }
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getPreferredName() {
        return alias == null ? fileName : alias;
    }

    @Override
    public CSVRecord getFirstLine() {
        if (csvRecords.size() > 0) return csvRecords.get(0);
        return null;
    }

    @Override
    public InputStreamReader getReader() throws ValidatorFileException {
        try {
            return readerMaker.makeReader(fileName);
        }
        catch (IOException e) {
            throw new ValidatorFileException(locale().ioException(fileName));
        }
    }

    @Override
    public Iterator<CSVRecord> iterator() {
        return csvRecords.stream().iterator();
    }
}
