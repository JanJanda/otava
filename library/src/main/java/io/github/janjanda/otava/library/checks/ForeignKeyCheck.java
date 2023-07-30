package io.github.janjanda.otava.library.checks;

import static io.github.janjanda.otava.library.utils.UrlUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.CheckFactory;
import io.github.janjanda.otava.library.Manager;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.CheckRunException;
import io.github.janjanda.otava.library.utils.DescriptorUtils;
import io.github.janjanda.otava.library.utils.Pair;
import io.github.janjanda.otava.library.utils.TableUtils;
import org.apache.commons.csv.CSVRecord;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class checks specified foreign keys between tables.
 * @see <a href="https://www.w3.org/TR/2015/REC-tabular-metadata-20151217/#foreign-key-reference-between-tables">Foreign keys</a>
 */
public final class ForeignKeyCheck extends Check {
    public ForeignKeyCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(PrimaryKeyCheck.class));
    }

    @Override
    protected Result performValidation() throws CheckRunException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
            Pair<Descriptor, JsonNode> tableMeta = DescriptorUtils.findTableDescriptorAndDescriptionWithExc(table, descriptors, this.getClass().getName());
            Descriptor tableDescriptor = tableMeta.first;
            JsonNode tableDescription = tableMeta.second;
            JsonNode[] foreignKeys = extractForeignKeys(tableDescription);
            for (JsonNode foreignKey : foreignKeys) {
                List<JsonNode> fKeyTitles = findColumnTitles(foreignKey.path("columnReference"), tableDescription, resultBuilder);
                List<Integer> fKeyColIndices = DescriptorUtils.findColumnsWithTitles(fKeyTitles, table, this.getClass().getName());
                Table referencedTable = findReferencedTable(foreignKey, tableDescriptor, tableDescription.path("url").asText(), resultBuilder);
                if (referencedTable != null) {
                    JsonNode refTableDescription = DescriptorUtils.findTableDescriptionWithExc(referencedTable, descriptors, this.getClass().getName());
                    List<JsonNode> refTableTitles = findColumnTitles(foreignKey.path("reference").path("columnReference"), refTableDescription, resultBuilder);
                    List<Integer> refTableColIndices = DescriptorUtils.findColumnsWithTitles(refTableTitles, referencedTable, this.getClass().getName());
                    checkForeignKey(table, fKeyColIndices, referencedTable, refTableColIndices, resultBuilder, tableDescription.path("url").asText());
                }
            }
        }
        return resultBuilder.build();
    }

    private JsonNode[] extractForeignKeys(JsonNode tableDescription) {
        JsonNode foreignKeys = tableDescription.path("tableSchema").path("foreignKeys");
        if (!foreignKeys.isArray()) return new JsonNode[0];
        JsonNode[] extracted = new JsonNode[foreignKeys.size()];
        for (int i = 0; i < extracted.length; i++) {
            extracted[i] = foreignKeys.path(i);
        }
        return extracted;
    }

    private List<JsonNode> findColumnTitles(JsonNode columnReference, JsonNode tableDescription, Result.Builder resultBuilder) {
        String[] referencedCols = DescriptorUtils.extractColumnReference(columnReference, resultBuilder, Manager.locale().badColRefInFKey(tableDescription.path("url").asText()));
        List<JsonNode> titleNodes = new ArrayList<>();
        for (String name : referencedCols) {
            JsonNode titles = DescriptorUtils.getTitlesForName(name, tableDescription);
            if (titles.isMissingNode()) resultBuilder.addMessage(Manager.locale().missingFKeyTitles(name, tableDescription.path("url").asText()));
            else titleNodes.add(titles);
        }
        return titleNodes;
    }

    private Table findReferencedTable(JsonNode foreignKey, Descriptor descriptor, String tableUrl, Result.Builder resultBuilder) {
        JsonNode resource = foreignKey.path("reference").path("resource");
        if (resource.isTextual()) {
            String resourceTextual = resource.asText();
            try {
                String refTableUrl = resolveUrl(getBaseUrl(descriptor), resourceTextual);
                for (Table table : tables) {
                    if (refTableUrl.equals(table.getPreferredName())) return table;
                }
            } catch (MalformedURLException e) {
                resultBuilder.addMessage(Manager.locale().badRefTableUrl(resourceTextual, tableUrl));
                return null;
            }
        }
        resultBuilder.addMessage(Manager.locale().missingFKeyTable(tableUrl));
        return null;
    }

    private void checkForeignKey(Table table, List<Integer> columns, Table refTable, List<Integer> refCols, Result.Builder resultBuilder, String tableUrl) {
        if (columns.size() != refCols.size()) {
            resultBuilder.addMessage(Manager.locale().badNumOfFKeyCols(tableUrl));
            return;
        }
        int[] referenceCols = new int[refCols.size()];
        for (int i = 0; i < referenceCols.length; i++) {
            referenceCols[i] = refCols.get(i);
        }
        String[] values = new String[columns.size()];
        for (CSVRecord row : table) {
            if (row.getRecordNumber() != 1) {
                for (int i = 0; i < values.length; i++) {
                    values[i] = row.get(columns.get(i));
                }
                if (TableUtils.areValuesInColumns(refTable, values, referenceCols, 1)) {
                    resultBuilder.addMessage(Manager.locale().fKeyViolation(tableUrl, refTable.getPreferredName()));
                    return;
                }
            }
        }
    }
}
