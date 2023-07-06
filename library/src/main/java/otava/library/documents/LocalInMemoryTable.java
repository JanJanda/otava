package otava.library.documents;

import static otava.library.utils.FileUtils.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import otava.library.*;
import otava.library.exceptions.ValidatorFileException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

public final class LocalInMemoryTable implements Table {
    private final String fileName;
    private final String alias;
    private List<CSVRecord> records;

    public LocalInMemoryTable(String fileName, CSVFormat csvFormat, String alias) throws IOException {
        this.fileName = fileName;
        this.alias = alias;
        fillCells(csvFormat);
    }

    private void fillCells(CSVFormat csvFormat) throws IOException {
        try (InputStreamReader reader = makeReader(fileName);
             CSVParser csvParser = CSVParser.parse(reader, csvFormat)) {
            records = csvParser.getRecords();
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
    public int getWidth() {
        if (records.size() > 0) return records.get(0).size();
        else return 0;
    }

    @Override
    public int getHeight() {
        return records.size();
    }

    @Override
    public String getCell(int row, int column) {
        return records.get(row).get(column);
    }

    @Override
    public CSVRecord getFirstLine() {
        if (records.size() > 0) return records.get(0);
        return null;
    }

    @Override
    public InputStreamReader getReader() throws ValidatorFileException {
        try {
            return makeReader(fileName);
        }
        catch (FileNotFoundException e) {
            throw new ValidatorFileException(Manager.locale().missingFile(fileName));
        }
    }

    @Override
    public Iterator<CSVRecord> iterator() {
        return records.stream().iterator();
    }
}
