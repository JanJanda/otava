package otava.library.checks;

import static otava.library.utils.UrlUtils.*;
import static otava.library.utils.DescriptorUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import otava.library.exceptions.CheckCreationException;
import otava.library.*;
import otava.library.documents.*;
import java.net.MalformedURLException;
import java.util.List;

public final class ColumnTitlesCheck extends Check {
    public ColumnTitlesCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(TableDescriptorCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder();
        if (fatalSubResult()) return resultBuilder.setSkipped().build();

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
}
