package io.github.janjanda.otava.library.utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.documents.Descriptor;

import java.net.MalformedURLException;
import java.net.URL;

public final class UrlUtils {
    private UrlUtils() {}

    public static String resolveUrl(String base, String spec) throws MalformedURLException {
        return new URL(new URL(base), spec).toString();
    }

    public static String getBaseUrl(Descriptor desc) {
        try {
            return getBaseUrlWithExc(desc);
        }
        catch (MalformedURLException e) {
            return "";
        }
    }

    public static String getBaseUrlWithExc(Descriptor desc) throws MalformedURLException {
        JsonNode baseNode = desc.path("@context").path(1).path("@base");
        if (baseNode.isTextual()) return resolveUrl(desc.getPreferredName(), baseNode.asText());
        return desc.getPreferredName();
    }
}
