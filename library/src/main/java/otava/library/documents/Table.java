package otava.library.documents;

import org.apache.commons.csv.CSVRecord;
import otava.library.exceptions.ValidatorFileException;
import java.io.InputStreamReader;

public interface Table extends Document, Iterable<CSVRecord> {
    int getWidth();
    int getHeight();
    String getCell(int row, int column);
    CSVRecord getFirstLine();
    InputStreamReader getReader() throws ValidatorFileException;
    boolean areValuesInColumns(String[] values, int[] columns, long ignoreRow);
}
