package io.github.janjanda.otava.library.documents;

import io.github.janjanda.otava.library.exceptions.FileIteratorException;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import static io.github.janjanda.otava.library.Manager.locale;

public final class RemoteTable implements Table {
    private final String fileName;
    private final String alias;
    private final CSVFormat csvFormat;
    private final ReaderMaker readerMaker;

    public RemoteTable(String fileName, String alias, CSVFormat csvFormat, ReaderMaker readerMaker) {
        this.fileName = fileName;
        this.alias = alias;
        this.csvFormat = csvFormat;
        this.readerMaker = readerMaker;
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
    public CSVRecord getFirstLine() throws ValidatorFileException {
        try (InputStreamReader reader = readerMaker.makeReader(fileName);
             CSVParser csvParser = CSVParser.parse(reader, csvFormat)) {
            Iterator<CSVRecord> i = csvParser.iterator();
            if (i.hasNext()) return i.next();
            return null;
        }
        catch (IOException e) {
            throw new ValidatorFileException(locale().ioException(fileName));
        }
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
        try {
            CSVParser csvParser = CSVParser.parse(readerMaker.makeReader(fileName), csvFormat);
            return csvParser.iterator();
        }
        catch (IOException e) {
            throw new FileIteratorException(locale().ioException(fileName));
        }
    }
}
