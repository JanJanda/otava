package io.github.janjanda.otava.library.checks;

import io.github.janjanda.otava.library.CheckFactory;
import io.github.janjanda.otava.library.Manager;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import org.apache.commons.csv.CSVRecord;

/**
 * This class checks size of rows in tables.
 * Each row in a table must have the same number of cells.
 * It also checks whether tables are not empty.
 */
public final class ConsistentColumnsCheck extends Check {
    public ConsistentColumnsCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(LineBreaksCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
            CSVRecord firstLine = table.getFirstLine();
            if (firstLine == null) resultBuilder.setFatal().addMessage(Manager.locale().emptyTable(table.getName()));
            else {
                int numberOfCols = firstLine.size();
                for (CSVRecord row : table) {
                    if (row.size() != numberOfCols) resultBuilder.setFatal().addMessage(Manager.locale().badRowSize(table.getName(), Long.toString(row.getRecordNumber())));
                }
            }
        }
        return resultBuilder.build();
    }
}
