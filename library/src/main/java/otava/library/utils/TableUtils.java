package otava.library.utils;

import org.apache.commons.csv.CSVRecord;
import otava.library.documents.Table;

public final class TableUtils {
    private TableUtils() {}

    /**
     * Checks whether there are specified values in all specified columns of the table.
     * @param values Specified values of individual cells.
     * @param columns The columns where the values should be. The value {@code values[i]} should be in the column {@code columns[i]}. Both arrays should have the same length. Column numbers are 0-based.
     * @param ignoreRow This row in the table is ignored during the search. Row number is 1-based.
     * @return {@code true} if there is a row (record) with the specified values in the specified columns, {@code false} otherwise
     */
    public static boolean areValuesInColumns(Table table, String[] values, int[] columns, long ignoreRow) {
        for (CSVRecord record : table) {
            if (record.getRecordNumber() != ignoreRow) {
                boolean match = true;
                for (int i = 0; i < columns.length; i++) {
                    if (!values[i].equals(record.get(columns[i]))) {
                        match = false;
                        break;
                    }
                }
                if (match) return true;
            }
        }
        return false;
    }
}
