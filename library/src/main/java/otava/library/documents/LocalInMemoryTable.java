package otava.library.documents;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import otava.library.*;
import otava.library.exceptions.ValidatorFileException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
        List<CSVRecord> records = csvFormat.parse(makeReader()).getRecords();
        cells = new String[records.size()][];
        int index = 0;
        for (CSVRecord record : records) {
            cells[index] = record.values();
            index++;
        }
    }

    private InputStreamReader makeReader() throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
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
    public InputStreamReader getReader() throws ValidatorFileException {
        try {
            return makeReader();
        }
        catch (FileNotFoundException e) {
            throw new ValidatorFileException(Manager.locale().missingFile(fileName));
        }
    }
}
