package otava.library.checks;

import static otava.library.utils.DescriptorUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.apache.commons.csv.CSVRecord;
import otava.library.*;
import otava.library.documents.Table;
import otava.library.exceptions.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PrimaryKeyCheck extends Check {
    public PrimaryKeyCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(ColumnTitlesCheck.class));
    }

    @Override
    protected Result performValidation() throws CheckRunException {
        Result.Builder resultBuilder = new Result.Builder();
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
            JsonNode tableDescription = findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
            String[] primaryKey = extractPrimaryKey(tableDescription, resultBuilder);
            List<JsonNode> titleNodes = new ArrayList<>();
            for (String pkColumn : primaryKey) {
                JsonNode titles = getTitlesForName(pkColumn, tableDescription);
                if (titles.isMissingNode()) resultBuilder.addMessage(Manager.locale().missingPrimKeyTitles(pkColumn, tableDescription.path("url").asText()));
                else titleNodes.add(titles);
            }
            List<Integer> pkColIndices = new ArrayList<>();
            CSVRecord firstLine = table.getFirstLine();
            for (JsonNode titleNode : titleNodes) {
                int index = findColumnWithTitle(firstLine, titleNode);
                if (index == -1) throw new CheckRunException(Manager.locale().checkRunException(this.getClass().getName()));
                else pkColIndices.add(index);
            }

        }

        return null;
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

    private int findColumnWithTitle(CSVRecord firstLine, JsonNode titles) {
        for (int i = 0; i < firstLine.size(); i++) {
            if (isStringInTitle(firstLine.get(i), titles)) return i;
        }
        return -1;
    }
}
