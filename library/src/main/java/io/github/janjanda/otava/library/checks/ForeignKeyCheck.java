package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.CheckFactory;
import io.github.janjanda.otava.library.Manager;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.CheckRunException;
import io.github.janjanda.otava.library.utils.DescriptorUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @see <a href="https://www.w3.org/TR/2015/REC-tabular-metadata-20151217/#schema-foreignKeys">Foreign keys</a>
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
            JsonNode tableDescription = DescriptorUtils.findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
            JsonNode[] foreignKeys = extractForeignKeys(tableDescription);
            for (JsonNode foreignKey : foreignKeys) {
                List<JsonNode> fKeyTitles = findFKeyTitles(foreignKey, tableDescription, resultBuilder);
                List<Integer> fKeyColIndices = DescriptorUtils.findColumnsWithTitles(fKeyTitles, table, this.getClass().getName());

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

    private List<JsonNode> findFKeyTitles(JsonNode foreignKey, JsonNode tableDescription, Result.Builder resultBuilder) {
        JsonNode columnReference = foreignKey.path("columnReference");
        String[] referencedCols = DescriptorUtils.extractColumnReference(columnReference, resultBuilder, Manager.locale().badColRefInFKey(tableDescription.path("url").asText()));
        List<JsonNode> titleNodes = new ArrayList<>();
        for (String name : referencedCols) {
            JsonNode titles = DescriptorUtils.getTitlesForName(name, tableDescription);
            if (titles.isMissingNode()) resultBuilder.addMessage(Manager.locale().missingFKeyTitles(name, tableDescription.path("url").asText()));
            else titleNodes.add(titles);
        }
        return titleNodes;
    }
}
