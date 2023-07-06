package otava.library.checks;

import static otava.library.utils.UrlUtils.*;
import static otava.library.utils.DescriptorUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.apache.commons.csv.CSVRecord;
import otava.library.exceptions.CheckCreationException;
import otava.library.*;
import otava.library.documents.*;
import otava.library.exceptions.CheckRunException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class checks columns in tables and their respective descriptors.
 * There should be columns with same titles in a table and its descriptor.
 */
public final class ColumnTitlesCheck extends Check {
    public ColumnTitlesCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(TableDescriptorCheck.class));
    }

    @Override
    protected Result performValidation() throws CheckRunException {
        Result.Builder resultBuilder = new Result.Builder();
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
            JsonNode tableDescription = findTableDescription(table);
            List<JsonNode> descColumns = extractNonVirtualColumns(tableDescription.path("tableSchema"));
            CSVRecord firstLine = table.getFirstLine();
            if (firstLine == null) resultBuilder.setFatal().addMessage(Manager.locale().emptyTable(table.getName()));
            else {
                for (String tableColName : firstLine) {
                    int indexFound = -1;
                    for (int i = 0; i < descColumns.size(); i++) {
                        if (isStringInTitle(tableColName, descColumns.get(i).path("titles"))) indexFound = i;
                    }
                    if (indexFound == -1) resultBuilder.addMessage(Manager.locale().missingColDesc(tableColName, table.getName()));
                    else descColumns.remove(indexFound);
                }
                for (JsonNode missedCol : descColumns) {
                    resultBuilder.setFatal().addMessage(Manager.locale().missingCol(missedCol.path("name").asText(), tableDescription.path("url").asText()));
                }
            }
        }
        return resultBuilder.build();
    }

    private List<JsonNode> extractNonVirtualColumns(JsonNode tableSchema) {
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

    private JsonNode findTableDescription(Table table) throws CheckRunException {
        String tableName = table.getPreferredName();
        for (Descriptor desc : descriptors) {
            String baseUrl = getBaseUrl(desc);
            List<JsonNode> tableNodes = extractTables(desc);
            for (JsonNode tableNode : tableNodes) {
                JsonNode urlNode = tableNode.path("url");
                try {
                    if (urlNode.isTextual() && resolveUrl(baseUrl, urlNode.asText()).equals(tableName)) return tableNode;
                }
                catch (MalformedURLException e) {
                    throw new CheckRunException(Manager.locale().checkRunException(this.getClass().getName()));
                }
            }
        }
        return MissingNode.getInstance();
    }

    private boolean isStringInTitle(String value, JsonNode title) {
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

    private boolean isStringInArray(String value, JsonNode arrayNode) {
        for (int i = 0; i < arrayNode.size(); i++) {
            if (arrayNode.path(i).isTextual() && arrayNode.path(i).asText().equals(value)) return true;
        }
        return false;
    }
}
