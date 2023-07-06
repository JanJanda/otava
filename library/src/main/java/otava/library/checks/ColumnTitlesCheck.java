package otava.library.checks;

import static otava.library.utils.DescriptorUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.csv.CSVRecord;
import otava.library.exceptions.*;
import otava.library.*;
import otava.library.documents.*;
import java.util.ArrayList;
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
            JsonNode tableDescription = findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
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
}
