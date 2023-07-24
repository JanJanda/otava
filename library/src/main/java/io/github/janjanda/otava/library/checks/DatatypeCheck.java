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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This class checks whether values in tables match their defined data types. Most common data types are supported.
 * @see <a href="https://www.w3.org/TR/2015/REC-tabular-metadata-20151217/#datatypes">Datatypes</a>
 */
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
            if (textual.equals("number") && !isNumber(value)) return false;
            if (textual.equals("integer") && !isInteger(value)) return false;
        }
        if (datatype.isObject()) {
            JsonNode baseNode = datatype.path("base");
            String base = "string";
            if (baseNode.isTextual()) base = baseNode.asText();
            if (base.equals("number")) return checkNumber(value, datatype);
            if (base.equals("integer")) return isInteger(value) && checkNumber(value, datatype);
            if (base.equals("string")) return checkString(value, datatype);
            if (base.equals("date")) return checkDate(value, datatype);
            if (base.equals("datetime")) return checkDateTime(value, datatype) || checkOffsetDateTime(value, datatype);
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

    private boolean isNumber(String value) {
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

    private boolean checkNumber(String value, JsonNode datatype) {
        if (!isNumber(value)) return false;
        double numValue = Double.parseDouble(value);
        String min = datatype.path("minimum").asText();
        if (isNumber(min) && Double.parseDouble(min) > numValue) return false;
        String minInc = datatype.path("minInclusive").asText();
        if (isNumber(minInc) && Double.parseDouble(minInc) > numValue) return false;
        String max = datatype.path("maximum").asText();
        if (isNumber(max) && Double.parseDouble(max) < numValue) return false;
        String maxInc = datatype.path("maxInclusive").asText();
        if (isNumber(maxInc) && Double.parseDouble(maxInc) < numValue) return false;
        String minEx = datatype.path("minExclusive").asText();
        if (isNumber(minEx) && Double.parseDouble(minEx) >= numValue) return false;
        String maxEx = datatype.path("maxExclusive").asText();
        if (isNumber(maxEx) && Double.parseDouble(maxEx) <= numValue) return false;
        return true;
    }

    private boolean checkString(String value, JsonNode datatype) {
        String length = datatype.path("length").asText();
        if (isInteger(length) && Integer.parseInt(length) != value.length()) return false;
        String minLength = datatype.path("minLength").asText();
        if (isInteger(minLength) && Integer.parseInt(minLength) > value.length()) return false;
        String maxLength = datatype.path("maxLength").asText();
        if (isInteger(maxLength) && Integer.parseInt(maxLength) < value.length()) return false;
        JsonNode formatNode = datatype.path("format");
        if (formatNode.isTextual()) {
            try {
                Pattern p = Pattern.compile(formatNode.asText());
                Matcher m = p.matcher(value);
                if (!m.matches()) return false;
            }
            catch (PatternSyntaxException ignored) {}
        }
        return true;
    }

    private boolean checkDate(String value, JsonNode datatype) {
        JsonNode formatNode = datatype.path("format");
        if (formatNode.isTextual()) {
            String format = formatNode.asText();
            try {
                LocalDate.parse(value, DateTimeFormatter.ofPattern(format));
            }
            catch (DateTimeParseException e) {
                return false;
            }
            catch (IllegalArgumentException ignored) {}
        }
        return true;
    }

    private boolean checkDateTime(String value, JsonNode datatype) {
        JsonNode formatNode = datatype.path("format");
        if (formatNode.isTextual()) {
            String format = formatNode.asText();
            try {
                LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format));
            }
            catch (DateTimeParseException e) {
                return false;
            }
            catch (IllegalArgumentException ignored) {}
        }
        return true;
    }

    private boolean checkOffsetDateTime(String value, JsonNode datatype) {
        JsonNode formatNode = datatype.path("format");
        if (formatNode.isTextual()) {
            String format = formatNode.asText();
            try {
                OffsetDateTime.parse(value, DateTimeFormatter.ofPattern(format));
            }
            catch (DateTimeParseException e) {
                return false;
            }
            catch (IllegalArgumentException ignored) {}
        }
        return true;
    }
}
