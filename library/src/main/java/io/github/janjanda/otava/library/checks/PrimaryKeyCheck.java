package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.CheckFactory;
import io.github.janjanda.otava.library.Manager;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.CheckRunException;
import io.github.janjanda.otava.library.utils.DescriptorUtils;
import io.github.janjanda.otava.library.utils.TableUtils;
import org.apache.commons.csv.CSVRecord;
import java.util.ArrayList;
import java.util.List;

/**
 * This class checks the defined primary keys of the tables. Values of a primary key must be unique.
 */
public final class PrimaryKeyCheck extends Check {
    public PrimaryKeyCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(ColumnTitlesCheck.class));
    }

    @Override
    protected Result performValidation() throws CheckRunException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
            List<JsonNode> columnNodes = findColumnNodes(table, resultBuilder);
            List<Integer> pkColIndices = DescriptorUtils.findColumnsWithDescriptions(columnNodes, table, this.getClass().getName());
            checkPrimKeyValues(table, pkColIndices, resultBuilder);
        }
        return resultBuilder.build();
    }

    private List<JsonNode> findColumnNodes(Table table, Result.Builder resultBuilder) throws CheckRunException {
        JsonNode tableDescription = DescriptorUtils.findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
        String[] primaryKey = extractPrimaryKey(tableDescription, resultBuilder);
        List<JsonNode> columnNodes = new ArrayList<>();
        for (String pkColumn : primaryKey) {
            JsonNode columnNode = DescriptorUtils.findColumnForName(pkColumn, tableDescription);
            if (columnNode.isMissingNode()) resultBuilder.addMessage(Manager.locale().missingPrimKeyTitles(pkColumn, tableDescription.path("url").asText()));
            else columnNodes.add(columnNode);
        }
        return columnNodes;
    }

    private void checkPrimKeyValues(Table table, List<Integer> pkColIndices, Result.Builder resultBuilder) {
        if (pkColIndices.isEmpty()) return;
        int[] columns = new int[pkColIndices.size()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = pkColIndices.get(i);
        }
        String[] values = new String[columns.length];
        for (CSVRecord row : table) {
            if (row.getRecordNumber() != 1) {
                for (int i = 0; i < columns.length; i++) {
                    values[i] = row.get(columns[i]);
                }
                if (TableUtils.areValuesInColumns(table, values, columns, row.getRecordNumber())) {
                    resultBuilder.setFatal().addMessage(Manager.locale().invalidPrimKey(table.getName())).build();
                    return;
                }
            }
        }
    }

    private String[] extractPrimaryKey(JsonNode tableDescription, Result.Builder resultBuilder) {
        JsonNode primaryKey = tableDescription.path("tableSchema").path("primaryKey");
        if (primaryKey.isMissingNode()) return new String[0];
        return DescriptorUtils.extractColumnReference(primaryKey, resultBuilder, Manager.locale().badValueInPrimKey(tableDescription.path("url").asText()));
    }
}
