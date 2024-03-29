package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.CheckRunException;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import io.github.janjanda.otava.library.factories.CheckFactory;
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

import static io.github.janjanda.otava.library.Manager.locale;
import static io.github.janjanda.otava.library.utils.DescriptorUtils.*;

/**
 * This class checks whether values in tables match their defined data types. Most common data types are supported.
 * It supports the following built-in data types: {@code string}, {@code anyURI}, {@code boolean}, {@code date}, {@code datetime}, {@code number}, {@code integer}, {@code time}, {@code byte}, {@code unsignedLong}, {@code unsignedShort}, {@code unsignedByte}, {@code positiveInteger}, {@code negativeInteger}, {@code nonPositiveInteger}, {@code nonNegativeInteger}, {@code double}, {@code float}.
 * It also supports derived data types. The bases {@code number}, {@code integer}, {@code byte}, {@code unsignedLong}, {@code unsignedShort}, {@code unsignedByte}, {@code positiveInteger}, {@code negativeInteger}, {@code nonPositiveInteger}, {@code nonNegativeInteger}, {@code double}, {@code float} support minimum and maximum constraints (both inclusive and exclusive).
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
    protected Result.Builder performValidation() throws CheckRunException, ValidatorFileException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped();
        for (Table table : tables) {
            JsonNode tableDescription = findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
            List<JsonNode> columns = extractNonVirtualColumns(tableDescription.path("tableSchema"));
            JsonNode[] dataTypes = extractAndSortDataTypes(columns, table);
            checkDataTypes(dataTypes, table, resultBuilder);
        }
        return resultBuilder;
    }

    private JsonNode[] extractAndSortDataTypes(List<JsonNode> columns, Table table) throws ValidatorFileException {
        CSVRecord firstLine = table.getFirstLine();
        JsonNode[] dataTypes = new JsonNode[firstLine.size()];
        Arrays.fill(dataTypes, MissingNode.getInstance());
        for (JsonNode column : columns) {
            int colIndex = findColumnWithDescription(firstLine, column);
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
                        resultBuilder.setFatal().addMessage(locale().badDatatype(table.getName(), Long.toString(row.getRecordNumber()), Integer.toString(i + 1)));
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
            if (textual.equals("anyURI")) return isURI(value);
            if (textual.equals("boolean")) return isBoolean(value);
            if (textual.equals("date")) return isDate(value);
            if (textual.equals("datetime")) return isDateTime(value) || isOffsetDateTime(value);
            if (textual.equals("number")) return isNumber(value);
            if (textual.equals("integer")) return isInteger(value);
            if (textual.equals("time")) return isTime(value) || isOffsetTime(value);
            if (textual.equals("byte")) return isByte(value);
            if (textual.equals("unsignedLong")) return isUnsLong(value);
            if (textual.equals("unsignedShort")) return isUnsShort(value);
            if (textual.equals("unsignedByte")) return isUnsByte(value);
            if (textual.equals("positiveInteger")) return isPosInteger(value);
            if (textual.equals("negativeInteger")) return isNegInteger(value);
            if (textual.equals("nonPositiveInteger")) return isNonPosInteger(value);
            if (textual.equals("nonNegativeInteger")) return isNonNegInteger(value);
            if (textual.equals("double")) return isNumber(value);
            if (textual.equals("float")) return isFloat(value);

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
            if (base.equals("byte")) return isByte(value) && checkNumber(value, dataType);
            if (base.equals("unsignedLong")) return isUnsLong(value) && checkNumber(value, dataType);
            if (base.equals("unsignedShort")) return isUnsShort(value) && checkNumber(value, dataType);
            if (base.equals("unsignedByte")) return isUnsByte(value) && checkNumber(value, dataType);
            if (base.equals("positiveInteger")) return isPosInteger(value) && checkNumber(value, dataType);
            if (base.equals("negativeInteger")) return isNegInteger(value) && checkNumber(value, dataType);
            if (base.equals("nonPositiveInteger")) return isNonPosInteger(value) && checkNumber(value, dataType);
            if (base.equals("nonNegativeInteger")) return isNonNegInteger(value) && checkNumber(value, dataType);
            if (base.equals("double")) return checkNumber(value, dataType);
            if (base.equals("float")) return isFloat(value) && checkNumber(value, dataType);
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

    private boolean isByte(String value) {
        try {
            Byte.parseByte(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isUnsLong(String value) {
        try {
            long v = Long.parseLong(value);
            return v >= 0;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isUnsShort(String value) {
        try {
            int v = Integer.parseInt(value);
            return v >= 0 && v <= 65535;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isUnsByte(String value) {
        try {
            int v = Integer.parseInt(value);
            return v >= 0 && v <= 255;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isPosInteger(String value) {
        try {
            long v = Long.parseLong(value);
            return v >= 1;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isNegInteger(String value) {
        try {
            long v = Long.parseLong(value);
            return v <= -1;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isNonPosInteger(String value) {
        try {
            long v = Long.parseLong(value);
            return v <= 0;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isNonNegInteger(String value) {
        try {
            long v = Long.parseLong(value);
            return v >= 0;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        }
        catch (NumberFormatException e) {
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
