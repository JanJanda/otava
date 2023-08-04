package io.github.janjanda.otava.library.utils;

import static io.github.janjanda.otava.library.utils.UrlUtils.*;
import static io.github.janjanda.otava.library.Manager.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import io.github.janjanda.otava.library.*;
import io.github.janjanda.otava.library.documents.*;
import io.github.janjanda.otava.library.exceptions.CheckRunException;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import org.apache.commons.csv.CSVRecord;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class DescriptorUtils {
    private DescriptorUtils() {}

    public static List<JsonNode> extractTables(Descriptor descriptor) {
        List<JsonNode> tableNodes = new ArrayList<>();
        JsonNode urlNode = descriptor.path("url");
        if (urlNode.isTextual()) {
            tableNodes.add(descriptor.getRootNode());
            return tableNodes;
        }
        JsonNode tablesArray = descriptor.path("tables");
        if (tablesArray.isArray()) {
            for (int i = 0; i < tablesArray.size(); i++) {
                tableNodes.add(tablesArray.path(i));
            }
        }
        return tableNodes;
    }

    public static Pair<Descriptor, JsonNode> findTableDescriptorAndDescription(Table table, DocsGroup<Descriptor> descriptors) throws MalformedURLException {
        String tableName = table.getPreferredName();
        for (Descriptor desc : descriptors) {
            String baseUrl = getBaseUrl(desc);
            List<JsonNode> tableNodes = extractTables(desc);
            for (JsonNode tableNode : tableNodes) {
                JsonNode urlNode = tableNode.path("url");
                if (urlNode.isTextual() && resolveUrl(baseUrl, urlNode.asText()).equals(tableName)) return new Pair<>(desc, tableNode);
            }
        }
        return new Pair<>(null, MissingNode.getInstance());
    }

    public static Pair<Descriptor, JsonNode> findTableDescriptorAndDescriptionWithExc(Table table, DocsGroup<Descriptor> descriptors, String cause) throws CheckRunException {
        try {
            return findTableDescriptorAndDescription(table, descriptors);
        }
        catch (MalformedURLException e) {
            throw new CheckRunException(locale().checkRunException(cause));
        }
    }

    public static Descriptor findTableDescriptorWithExc(Table table, DocsGroup<Descriptor> descriptors, String cause) throws CheckRunException {
        try {
            return findTableDescriptorAndDescription(table, descriptors).first;
        }
        catch (MalformedURLException e) {
            throw new CheckRunException(locale().checkRunException(cause));
        }
    }

    public static JsonNode findTableDescriptionWithExc(Table table, DocsGroup<Descriptor> descriptors, String cause) throws CheckRunException {
        try {
            return findTableDescriptorAndDescription(table, descriptors).second;
        }
        catch (MalformedURLException e) {
            throw new CheckRunException(locale().checkRunException(cause));
        }
    }

    public static boolean isStringInName(String value, JsonNode name) {
        return name.isTextual() && name.asText().equals(value);
    }

    public static boolean isStringInTitle(String value, JsonNode title) {
        if (title.isTextual() && title.asText().equals(value)) return true;
        if (title.isArray() && isStringInArray(value, title)) return true;
        if (title.isObject()) {
            Iterator<String> fieldNames = title.fieldNames();
            while (fieldNames.hasNext()) {
                JsonNode item = title.path(fieldNames.next());
                if (item.isTextual() && item.asText().equals(value)) return true;
                if (item.isArray() && isStringInArray(value, item)) return true;
            }
        }
        return false;
    }

    public static boolean isStringInArray(String value, JsonNode arrayNode) {
        for (int i = 0; i < arrayNode.size(); i++) {
            if (arrayNode.path(i).isTextual() && arrayNode.path(i).asText().equals(value)) return true;
        }
        return false;
    }

    public static int findColumnWithDescription(CSVRecord firstLine, JsonNode colDescription) {
        for (int i = 0; i < firstLine.size(); i++) {
            String colTitle = firstLine.get(i);
            if (isStringInName(colTitle, colDescription.path("name")) || isStringInTitle(colTitle, colDescription.path("titles"))) return i;
        }
        return -1;
    }

    public static List<JsonNode> extractNonVirtualColumns(JsonNode tableSchema) {
        List<JsonNode> columns = new ArrayList<>();
        JsonNode colsNode = tableSchema.path("columns");
        if (colsNode.isArray()) {
            for (int i = 0; i < colsNode.size(); i++) {
                JsonNode column = colsNode.path(i);
                JsonNode virtualNode = column.path("virtual");
                if (!virtualNode.isBoolean() || !virtualNode.asBoolean()) columns.add(column);
            }
        }
        return columns;
    }

    public static String[] extractColumnReference(JsonNode columnReference, Result.Builder resultBuilder, String errorMessage) {
        if (columnReference.isTextual()) return new String[]{columnReference.asText()};
        if (columnReference.isArray()) {
            Set<String> cols = new HashSet<>();
            for (int i = 0; i < columnReference.size(); i++) {
                if (columnReference.path(i).isTextual()) cols.add(columnReference.path(i).asText());
                else resultBuilder.addMessage(errorMessage);
            }
            return cols.toArray(new String[0]);
        }
        resultBuilder.addMessage(errorMessage);
        return new String[0];
    }

    public static JsonNode findNonVirtColumnForName(String name, JsonNode tableDescription) {
        JsonNode cols = tableDescription.path("tableSchema").path("columns");
        if (cols.isArray()) {
            for (int i = 0; i < cols.size(); i++) {
                JsonNode nameNode = cols.path(i).path("name");
                JsonNode virtualNode = cols.path(i).path("virtual");
                boolean validColumn = (!virtualNode.isBoolean() || !virtualNode.asBoolean()) && nameNode.isTextual() && nameNode.asText().equals(name);
                if (validColumn) return cols.path(i);
            }
        }
        return MissingNode.getInstance();
    }

    public static List<Integer> findColumnsWithDescriptions(List<JsonNode> colsDescriptions, Table table, String caller) throws ValidatorFileException, CheckRunException {
        List<Integer> colIndices = new ArrayList<>();
        CSVRecord firstLine = table.getFirstLine();
        for (JsonNode colDesc : colsDescriptions) {
            int index = findColumnWithDescription(firstLine, colDesc);
            if (index == -1) throw new CheckRunException(locale().checkRunException(caller));
            else colIndices.add(index);
        }
        return colIndices;
    }
}
