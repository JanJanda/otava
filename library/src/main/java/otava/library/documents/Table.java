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

    /**
     * Checks whether there are specified values in specified columns of the table.
     * @param values Specified values of individual cells.
     * @param columns The columns where the values should be. The value {@code values[i]} should be in column {@code columns[i]}. Both arrays should have the same length. Column numbers are 0-based.
     * @param ignoreRow This row in the table is ignored during the search. Row number is 1-based.
     * @return {@code true} if there is a row (record) with the specified values in the specified columns, {@code false} otherwise
     */
    boolean areValuesInColumns(String[] values, int[] columns, long ignoreRow);
}
