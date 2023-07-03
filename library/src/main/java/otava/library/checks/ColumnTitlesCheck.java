package otava.library.checks;

import static otava.library.utils.UrlUtils.*;
import static otava.library.utils.DescriptorUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import otava.library.exceptions.CheckCreationException;
import otava.library.*;
import otava.library.documents.*;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

public final class ColumnTitlesCheck extends Check {
    public ColumnTitlesCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(TableDescriptorCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder();
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
           // JsonNode tableDescription = findTableDescription(table);
// implementovat Table.getHeader()
        }
        return null;
    }

    private JsonNode findTableDescription(Table table) throws MalformedURLException {
        String tableName = table.getPreferredName();
        for (Descriptor desc : descriptors) {
            String baseUrl = getBaseUrl(desc);
            List<JsonNode> tableNodes = extractTables(desc);
            for (JsonNode tableNode : tableNodes) {
                JsonNode urlNode = tableNode.path("url");
                if (urlNode.isTextual() && resolveUrl(baseUrl, urlNode.asText()).equals(tableName)) return tableNode;
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
