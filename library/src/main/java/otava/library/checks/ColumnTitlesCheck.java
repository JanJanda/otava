package otava.library.checks;

import static otava.library.utils.UrlUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import otava.library.exceptions.CheckCreationException;
import otava.library.*;
import otava.library.documents.*;
import java.net.MalformedURLException;

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
// TODO: vyclenit extrakci tabulek z daneho deskriptoru?
    private JsonNode findTableDescription(Table table) throws MalformedURLException {
        String tableName = table.getPreferredName();
        for (Descriptor desc : descriptors) {
            String baseUrl = getBaseUrl(desc);
            JsonNode urlNode = desc.path("url");
            if (urlNode.isTextual() && resolveUrl(baseUrl, urlNode.asText()).equals(tableName)) return desc.getRootNode();
            JsonNode tablesArray = desc.path("tables");
            if (urlNode.isMissingNode() && tablesArray.isArray()) {
                for (int i = 0; i < tablesArray.size(); i++) {
                    JsonNode arrayUrlNode = tablesArray.path(i).path("url");
                    if (arrayUrlNode.isTextual() && resolveUrl(baseUrl, arrayUrlNode.asText()).equals(tableName)) return tablesArray.path(i);
                }
            }
        }
        return null;
    }
}
