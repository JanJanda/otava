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
import java.util.List;

public final class LocalInMemoryTable implements Table {
    private final String fileName;
    private final String alias;
    private String[][] cells;

    public LocalInMemoryTable(String fileName, CSVFormat csvFormat, String alias) throws IOException {
        this.fileName = fileName;
        this.alias = alias;
        fillCells(csvFormat);
    }

    private void fillCells(CSVFormat csvFormat) throws IOException {
        try (InputStreamReader reader = makeReader(fileName);
             CSVParser csvParser = csvFormat.parse(reader)) {
            List<CSVRecord> records = csvParser.getRecords();
            cells = new String[records.size()][];
            int index = 0;
            for (CSVRecord record : records) {
                cells[index] = record.values();
                index++;
            }
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
        if (cells.length > 0) return cells[0].length;
        else return 0;
    }

    @Override
    public int getHeight() {
        return cells.length;
    }

    @Override
    public String getCell(int row, int column) {
        return cells[row][column];
    }

    @Override
    public String[] getFirstLine() {
        if (cells.length > 0) return cells[0].clone();
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
}
