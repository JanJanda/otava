package otava.library.utils;

import com.fasterxml.jackson.databind.JsonNode;
import otava.library.documents.Descriptor;
import java.util.ArrayList;
import java.util.List;

public final class DescriptorUtils {
    private DescriptorUtils() {}

    public static List<JsonNode> extractTables(Descriptor descriptor) {
        List<JsonNode> tableNodes = new ArrayList<>();
        JsonNode urlNode = descriptor.path("url");
        if (urlNode.isTextual()) {
            tableNodes.add(descriptor.getRootNode());
            return tableNodes;
        }
        JsonNode tablesArray = descriptor.path("tables");
        if (tablesArray.isArray()) {
            for (int i = 0; i < tablesArray.size(); i++) {
                tableNodes.add(tablesArray.path(i));
            }
        }
        return tableNodes;
    }
}
