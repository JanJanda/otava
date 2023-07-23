package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.CheckFactory;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.CheckRunException;
import io.github.janjanda.otava.library.utils.DescriptorUtils;
import org.apache.commons.csv.CSVRecord;
import java.util.List;

public final class DatatypeCheck extends Check {
    public DatatypeCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(ColumnTitlesCheck.class));
    }

    @Override
    protected Result performValidation() throws CheckRunException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
            JsonNode tableDescription = DescriptorUtils.findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
            List<JsonNode> columns = DescriptorUtils.extractNonVirtualColumns(tableDescription.path("tableSchema"));
            JsonNode[] datatypes = extractAndSortDatatypes(columns, table);
            checkDatatypes(datatypes, table, resultBuilder);
        }
        return resultBuilder.build();
    }

    private JsonNode[] extractAndSortDatatypes(List<JsonNode> columns, Table table) {
        JsonNode[] datatypes = new JsonNode[columns.size()];
        CSVRecord firstLine = table.getFirstLine();
        for (JsonNode column : columns) {
            int colIndex = DescriptorUtils.findColumnWithTitle(firstLine, column.path("titles"));
            datatypes[colIndex] = column.path("datatype");
        }
        return datatypes;
    }

    private void checkDatatypes(JsonNode[] datatypes, Table table, Result.Builder resultBuilder) {
        boolean notFirstLine = false;
        for (CSVRecord row : table) {
            if (notFirstLine) checkRow(row, datatypes, resultBuilder);
            notFirstLine = true;
        }
    }

    private void checkRow(CSVRecord row, JsonNode[] datatypes, Result.Builder resultBuilder) {
        for (int i = 0; i < datatypes.length; i++) {
            checkValue(row.get(i), datatypes[i], resultBuilder);
        }
    }

    private void checkValue(String value, JsonNode datatype, Result.Builder resultBuilder) {
        if (datatype.isTextual()) {
            String textual = datatype.asText();
            if (textual.equals("integer"));
        }
    }
}
