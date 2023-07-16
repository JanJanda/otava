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
import io.github.janjanda.otava.library.utils.TableUtils;
import org.apache.commons.csv.CSVRecord;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class checks the defined primary keys of the tables. The values of the primary key must be unique.
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
            List<JsonNode> titleNodes = findTitleNodes(table, resultBuilder);
            List<Integer> pkColIndices = findPrimKeyColumns(table, titleNodes);
            checkPrimKeyValues(table, pkColIndices, resultBuilder);
        }
        return resultBuilder.build();
    }

    private List<JsonNode> findTitleNodes(Table table, Result.Builder resultBuilder) throws CheckRunException {
        JsonNode tableDescription = DescriptorUtils.findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
        String[] primaryKey = extractPrimaryKey(tableDescription, resultBuilder);
        List<JsonNode> titleNodes = new ArrayList<>();
        for (String pkColumn : primaryKey) {
            JsonNode titles = getTitlesForName(pkColumn, tableDescription);
            if (titles.isMissingNode()) resultBuilder.addMessage(Manager.locale().missingPrimKeyTitles(pkColumn, tableDescription.path("url").asText()));
            else titleNodes.add(titles);
        }
        return titleNodes;
    }

    private List<Integer> findPrimKeyColumns(Table table, List<JsonNode> titleNodes) throws CheckRunException {
        List<Integer> pkColIndices = new ArrayList<>();
        CSVRecord firstLine = table.getFirstLine();
        for (JsonNode titleNode : titleNodes) {
            int index = DescriptorUtils.findColumnWithTitle(firstLine, titleNode);
            if (index == -1) throw new CheckRunException(Manager.locale().checkRunException(this.getClass().getName()));
            else pkColIndices.add(index);
        }
        return pkColIndices;
    }

    private void checkPrimKeyValues(Table table, List<Integer> pkColIndices, Result.Builder resultBuilder) {
        if (pkColIndices.isEmpty()) return;
        int[] columns = new int[pkColIndices.size()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = pkColIndices.get(i);
        }
        String[] values = new String[columns.length];
        for (CSVRecord row : table) {
            for (int i = 0; i < columns.length; i++) {
                values[i] = row.get(columns[i]);
            }
            if (TableUtils.areValuesInColumns(table, values, columns, row.getRecordNumber())) {
                resultBuilder.setFatal().addMessage(Manager.locale().invalidPrimKey(table.getName())).build();
                return;
            }
        }
    }

    private String[] extractPrimaryKey(JsonNode tableDescription, Result.Builder resultBuilder) {
        JsonNode primaryKey = tableDescription.path("tableSchema").path("primaryKey");
        if (primaryKey.isMissingNode()) return new String[0];
        if (primaryKey.isTextual()) return new String[]{primaryKey.asText()};
        if (primaryKey.isArray()) {
            Set<String> aux = new HashSet<>();
            for (int i = 0; i < primaryKey.size(); i++) {
                if (primaryKey.path(i).isTextual()) aux.add(primaryKey.path(i).asText());
                else resultBuilder.addMessage(Manager.locale().badValueInPrimKey(tableDescription.path("url").asText()));
            }
            return aux.toArray(new String[0]);
        }
        resultBuilder.addMessage(Manager.locale().badValueInPrimKey(tableDescription.path("url").asText()));
        return new String[0];
    }

    private JsonNode getTitlesForName(String name, JsonNode tableDescription) {
        JsonNode cols = tableDescription.path("tableSchema").path("columns");
        if (cols.isArray()) {
            for (int i = 0; i < cols.size(); i++) {
                JsonNode nameNode = cols.path(i).path("name");
                JsonNode virtualNode = cols.path(i).path("virtual");
                boolean validColumn = (!virtualNode.isBoolean() || !virtualNode.asBoolean()) && nameNode.isTextual() && nameNode.asText().equals(name);
                if (validColumn) return cols.path(i).path("titles");
            }
        }
        return MissingNode.getInstance();
    }
}
