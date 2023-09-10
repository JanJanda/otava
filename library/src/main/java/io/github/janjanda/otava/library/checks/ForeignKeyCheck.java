package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.CheckRunException;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import io.github.janjanda.otava.library.factories.CheckFactory;
import io.github.janjanda.otava.library.utils.Pair;
import org.apache.commons.csv.CSVRecord;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static io.github.janjanda.otava.library.Manager.locale;
import static io.github.janjanda.otava.library.utils.DescriptorUtils.*;
import static io.github.janjanda.otava.library.utils.TableUtils.areValuesInColumns;
import static io.github.janjanda.otava.library.utils.UrlUtils.getBaseUrl;
import static io.github.janjanda.otava.library.utils.UrlUtils.resolveUrl;

/**
 * This class checks specified foreign keys between tables.
 * @see <a href="https://www.w3.org/TR/2015/REC-tabular-metadata-20151217/#foreign-key-reference-between-tables">Foreign keys</a>
 */
public final class ForeignKeyCheck extends Check {
    public ForeignKeyCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(PrimaryKeyCheck.class));
    }

    @Override
    protected Result.Builder performValidation() throws CheckRunException, ValidatorFileException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped();
        for (Table table : tables) {
            Pair<Descriptor, JsonNode> tableMeta = findTableDescriptorAndDescriptionWithExc(table, descriptors, this.getClass().getName());
            Descriptor tableDescriptor = tableMeta.first;
            JsonNode tableDescription = tableMeta.second;
            String tableUrl = tableDescription.path("url").asText();
            JsonNode[] foreignKeys = extractForeignKeys(tableDescription);
            for (JsonNode foreignKey : foreignKeys) {
                if (checkFKeyStructure(foreignKey, resultBuilder, tableUrl)) {
                    List<JsonNode> fKeyCols = findColumnDescriptions(foreignKey.path("columnReference"), tableDescription, resultBuilder);
                    List<Integer> fKeyColIndices = findColumnsWithDescriptions(fKeyCols, table, this.getClass().getName());
                    Table referencedTable = findReferencedTable(foreignKey, tableDescriptor, tableUrl, resultBuilder);
                    if (referencedTable != null) {
                        JsonNode refTableDescription = findTableDescriptionWithExc(referencedTable, descriptors, this.getClass().getName());
                        List<JsonNode> refTableCols = findColumnDescriptions(foreignKey.path("reference").path("columnReference"), refTableDescription, resultBuilder);
                        List<Integer> refTableColIndices = findColumnsWithDescriptions(refTableCols, referencedTable, this.getClass().getName());
                        checkForeignKey(table, fKeyColIndices, referencedTable, refTableColIndices, resultBuilder, tableUrl);
                    }
                }
            }
        }
        return resultBuilder;
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

    private List<JsonNode> findColumnDescriptions(JsonNode columnReference, JsonNode tableDescription, Result.Builder resultBuilder) {
        String[] referencedCols = extractColumnReference(columnReference, resultBuilder, locale().badColRefInFKey(tableDescription.path("url").asText()));
        List<JsonNode> columnNodes = new ArrayList<>();
        for (String name : referencedCols) {
            JsonNode column = findNonVirtColumnForName(name, tableDescription);
            if (column.isMissingNode()) resultBuilder.setFatal().addMessage(locale().missingFKeyColDesc(name, tableDescription.path("url").asText()));
            else columnNodes.add(column);
        }
        return columnNodes;
    }

    private Table findReferencedTable(JsonNode foreignKey, Descriptor descriptor, String tableUrl, Result.Builder resultBuilder) {
        JsonNode fKeyRef = foreignKey.path("reference");
        if (!fKeyRef.isObject()) {
            resultBuilder.setFatal().addMessage(locale().propIsNotObject("reference", descriptor.getName()));
            return null;
        }
        JsonNode resource = fKeyRef.path("resource");
        if (resource.isTextual()) {
            String resourceTextual = resource.asText();
            try {
                String refTableUrl = resolveUrl(getBaseUrl(descriptor), resourceTextual);
                for (Table table : tables) {
                    if (refTableUrl.equals(table.getPreferredName())) return table;
                }
            }
            catch (MalformedURLException e) {
                resultBuilder.setFatal().addMessage(locale().badRefTableUrl(resourceTextual, tableUrl) + " --- " + e.getMessage());
                return null;
            }
        }
        resultBuilder.setFatal().addMessage(locale().missingFKeyTable(tableUrl));
        return null;
    }

    private void checkForeignKey(Table table, List<Integer> columns, Table refTable, List<Integer> refCols, Result.Builder resultBuilder, String tableUrl) {
        if (columns.size() != refCols.size()) {
            resultBuilder.setFatal().addMessage(locale().badNumOfFKeyCols(tableUrl));
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
                if (!areValuesInColumns(refTable, values, referenceCols, 1)) {
                    resultBuilder.setFatal().addMessage(locale().fKeyViolation(tableUrl, refTable.getPreferredName()));
                    return;
                }
            }
        }
    }

    private boolean checkFKeyStructure(JsonNode fKey, Result.Builder resultBuilder, String tableUrl) {
        if (!fKey.isObject()) {
            resultBuilder.setFatal().addMessage(locale().fKeyIsNotObject(tableUrl));
            return false;
        }
        if (fKey.size() > 2 || fKey.path("reference").size() > 2) {
            resultBuilder.setFatal().addMessage(locale().extraPropsInFKey(tableUrl));
            return false;
        }
        return true;
    }
}
