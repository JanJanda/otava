package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This class checks whether values in tables match their defined data types. Most common data types are supported.
 * It supports the following built-in data types: {@code string}, {@code anyURI}, {@code boolean}, {@code date}, {@code datetime}, {@code number}, {@code integer}, {@code time}.
 * It also supports derived data types. The bases {@code number} and {@code integer} support minimum and maximum constraints (both inclusive and exclusive).
 * The base {@code string} supports length, minimum length, maximum length and format constraints. The format expects a regular expression.
 * The bases {@code date}, {@code time} and {@code datetime} support format constraint. The format expects a datetime formatter pattern.
 * Unsupported features and invalid constraints are ignored because they may still be reasonable outside the scope of this validator.
 * @see <a href="https://www.w3.org/TR/2015/REC-tabular-metadata-20151217/#datatypes">Data types</a>
 */
public final class DataTypesCheck extends Check {
    public DataTypesCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(ColumnTitlesCheck.class));
    }

    @Override
    protected Result performValidation() throws CheckRunException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
            JsonNode tableDescription = DescriptorUtils.findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
            List<JsonNode> columns = DescriptorUtils.extractNonVirtualColumns(tableDescription.path("tableSchema"));
            JsonNode[] dataTypes = extractAndSortDataTypes(columns, table);
            checkDataTypes(dataTypes, table, resultBuilder);
        }
        return resultBuilder.build();
    }

    private JsonNode[] extractAndSortDataTypes(List<JsonNode> columns, Table table) {
        CSVRecord firstLine = table.getFirstLine();
        JsonNode[] dataTypes = new JsonNode[firstLine.size()];
        Arrays.fill(dataTypes, MissingNode.getInstance());
        for (JsonNode column : columns) {
            int colIndex = DescriptorUtils.findColumnWithTitle(firstLine, column.path("titles"));
            dataTypes[colIndex] = column.path("datatype");
        }
        return dataTypes;
    }

    private void checkDataTypes(JsonNode[] dataTypes, Table table, Result.Builder resultBuilder) {
        boolean notFirstLine = false;
        for (CSVRecord row : table) {
            if (notFirstLine) {
                for (int i = 0; i < dataTypes.length; i++) {
                    if (!okValue(row.get(i), dataTypes[i])) {
                        resultBuilder.addMessage(Manager.locale().badDatatype(table.getName(), Long.toString(row.getRecordNumber()), Integer.toString(i + 1)));
                    }
                }
            }
            notFirstLine = true;
        }
    }

    private boolean okValue(String value, JsonNode dataType) {
        if (value.isEmpty()) return true;
        if (dataType.isTextual()) {
            String textual = dataType.asText();
            if (textual.equals("anyURI") && !isURI(value)) return false;
            if (textual.equals("boolean") && !isBoolean(value)) return false;
            if (textual.equals("date") && !isDate(value)) return false;
            if (textual.equals("datetime") && !isDateTime(value) && !isOffsetDateTime(value)) return false;
            if (textual.equals("number") && !isNumber(value)) return false;
            if (textual.equals("integer") && !isInteger(value)) return false;
            if (textual.equals("time") && !isTime(value) && !isOffsetTime(value)) return false;
        }
        if (dataType.isObject()) {
            JsonNode baseNode = dataType.path("base");
            String base = "string";
            if (baseNode.isTextual()) base = baseNode.asText();
            if (base.equals("number")) return checkNumber(value, dataType);
            if (base.equals("integer")) return isInteger(value) && checkNumber(value, dataType);
            if (base.equals("string")) return checkString(value, dataType);
            if (base.equals("date")) return checkDate(value, dataType);
            if (base.equals("datetime")) return checkDateTime(value, dataType) || checkOffsetDateTime(value, dataType);
            if (base.equals("time")) return checkTime(value, dataType) || checkOffsetTime(value, dataType);
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

    private boolean isTime(String value) {
        try {
            LocalTime.parse(value);
            return true;
        }
        catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isOffsetTime(String value) {
        try {
            OffsetTime.parse(value);
            return true;
        }
        catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean checkNumber(String value, JsonNode dataType) {
        if (!isNumber(value)) return false;
        double numValue = Double.parseDouble(value);
        String min = dataType.path("minimum").asText();
        if (isNumber(min) && Double.parseDouble(min) > numValue) return false;
        String minInc = dataType.path("minInclusive").asText();
        if (isNumber(minInc) && Double.parseDouble(minInc) > numValue) return false;
        String max = dataType.path("maximum").asText();
        if (isNumber(max) && Double.parseDouble(max) < numValue) return false;
        String maxInc = dataType.path("maxInclusive").asText();
        if (isNumber(maxInc) && Double.parseDouble(maxInc) < numValue) return false;
        String minEx = dataType.path("minExclusive").asText();
        if (isNumber(minEx) && Double.parseDouble(minEx) >= numValue) return false;
        String maxEx = dataType.path("maxExclusive").asText();
        if (isNumber(maxEx) && Double.parseDouble(maxEx) <= numValue) return false;
        return true;
    }

    private boolean checkString(String value, JsonNode dataType) {
        String length = dataType.path("length").asText();
        if (isInteger(length) && Integer.parseInt(length) != value.length()) return false;
        String minLength = dataType.path("minLength").asText();
        if (isInteger(minLength) && Integer.parseInt(minLength) > value.length()) return false;
        String maxLength = dataType.path("maxLength").asText();
        if (isInteger(maxLength) && Integer.parseInt(maxLength) < value.length()) return false;
        JsonNode formatNode = dataType.path("format");
        if (formatNode.isTextual()) {
            try {
                if (!Pattern.matches(formatNode.asText(), value)) return false;
            }
            catch (PatternSyntaxException ignored) {}
        }
        return true;
    }

    private boolean checkDate(String value, JsonNode dataType) {
        JsonNode formatNode = dataType.path("format");
        if (formatNode.isTextual()) {
            try {
                LocalDate.parse(value, DateTimeFormatter.ofPattern(formatNode.asText()));
            }
            catch (DateTimeParseException e) {
                return false;
            }
            catch (IllegalArgumentException ignored) {}
        }
        return true;
    }

    private boolean checkDateTime(String value, JsonNode dataType) {
        JsonNode formatNode = dataType.path("format");
        if (formatNode.isTextual()) {
            try {
                LocalDateTime.parse(value, DateTimeFormatter.ofPattern(formatNode.asText()));
            }
            catch (DateTimeParseException e) {
                return false;
            }
            catch (IllegalArgumentException ignored) {}
        }
        return true;
    }

    private boolean checkOffsetDateTime(String value, JsonNode dataType) {
        JsonNode formatNode = dataType.path("format");
        if (formatNode.isTextual()) {
            try {
                OffsetDateTime.parse(value, DateTimeFormatter.ofPattern(formatNode.asText()));
            }
            catch (DateTimeParseException e) {
                return false;
            }
            catch (IllegalArgumentException ignored) {}
        }
        return true;
    }

    private boolean checkTime(String value, JsonNode dataType) {
        JsonNode formatNode = dataType.path("format");
        if (formatNode.isTextual()) {
            try {
                LocalTime.parse(value, DateTimeFormatter.ofPattern(formatNode.asText()));
            }
            catch (DateTimeParseException e) {
                return false;
            }
            catch (IllegalArgumentException ignored) {}
        }
        return true;
    }

    private boolean checkOffsetTime(String value, JsonNode dataType) {
        JsonNode formatNode = dataType.path("format");
        if (formatNode.isTextual()) {
            try {
                OffsetTime.parse(value, DateTimeFormatter.ofPattern(formatNode.asText()));
            }
            catch (DateTimeParseException e) {
                return false;
            }
            catch (IllegalArgumentException ignored) {}
        }
        return true;
    }
}
