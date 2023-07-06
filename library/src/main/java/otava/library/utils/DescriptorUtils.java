package otava.library.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import otava.library.Manager;
import otava.library.documents.*;
import otava.library.exceptions.CheckRunException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static otava.library.utils.UrlUtils.getBaseUrl;
import static otava.library.utils.UrlUtils.resolveUrl;

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

    public static JsonNode findTableDescription(Table table, DocsGroup<Descriptor> descriptors) throws MalformedURLException {
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

    public static JsonNode findTableDescriptionWithExc(Table table, DocsGroup<Descriptor> descriptors, String cause) throws CheckRunException {
        try {
            return findTableDescription(table, descriptors);
        }
        catch (MalformedURLException e) {
            throw new CheckRunException(Manager.locale().checkRunException(cause));
        }
    }

    public static boolean isStringInTitle(String value, JsonNode title) {
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

    public static boolean isStringInArray(String value, JsonNode arrayNode) {
        for (int i = 0; i < arrayNode.size(); i++) {
            if (arrayNode.path(i).isTextual() && arrayNode.path(i).asText().equals(value)) return true;
        }
        return false;
    }
}
