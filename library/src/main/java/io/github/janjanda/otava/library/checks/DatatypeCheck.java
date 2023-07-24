package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.CheckFactory;
import io.github.janjanda.otava.library.Manager;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.CheckRunException;
import io.github.janjanda.otava.library.utils.DescriptorUtils;
import org.apache.commons.csv.CSVRecord;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
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
            if (notFirstLine) checkRow(row, datatypes, resultBuilder, table.getName());
            notFirstLine = true;
        }
    }

    private void checkRow(CSVRecord row, JsonNode[] datatypes, Result.Builder resultBuilder, String tableName) {
        for (int i = 0; i < datatypes.length; i++) {
            if (!okValue(row.get(i), datatypes[i])) resultBuilder.addMessage(Manager.locale().badDatatype(tableName, Long.toString(row.getRecordNumber()), Integer.toString(i)));
        }
    }

    private boolean okValue(String value, JsonNode datatype) {
        if (datatype.isTextual()) {
            String textual = datatype.asText();
            if (textual.equals("anyURI") && !isURI(value)) return false;
            if (textual.equals("boolean") && !isBoolean(value)) return false;
            if (textual.equals("date") && !isDate(value)) return false;
            if (textual.equals("datetime") && !isDateTime(value) && !isOffsetDateTime(value)) return false;
            if (textual.equals("number") && !isDouble(value)) return false;
            if (textual.equals("integer") && !isInteger(value)) return false;
        }
        return true;
    }

    private boolean isURI(String value) {
        try {
            new URI(value);
            return true;
        }
        catch (URISyntaxException e) {
            return false;
        }
    }

    private boolean isBoolean(String value) {
        return value.equals("true") || value.equals("false") || value.equals("1") || value.equals("0");
    }

    private boolean isDate(String value) {
        try {
            LocalDate.parse(value);
            return true;
        }
        catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isDateTime(String value) {
        try {
            LocalDateTime.parse(value);
            return true;
        }
        catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isOffsetDateTime(String value) {
        try {
            OffsetDateTime.parse(value);
            return true;
        }
        catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
