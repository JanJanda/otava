package otava.library.utils;

import com.fasterxml.jackson.databind.JsonNode;
import otava.library.documents.*;
import java.net.MalformedURLException;
import java.net.URL;

public final class UrlUtils {
    private UrlUtils() {}

    public static String resolveUrl(String context, String spec) throws MalformedURLException {
        return new URL(new URL(context), spec).toString();
    }

    public static String getBaseUrl(Descriptor desc) {
        JsonNode baseNode = desc.path("@context").path(1).path("@base");
        if (baseNode.isTextual()) return baseNode.asText();
        return desc.getPreferredName();
    }
}
