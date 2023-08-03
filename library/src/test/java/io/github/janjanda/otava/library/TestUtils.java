package io.github.janjanda.otava.library;

import io.github.janjanda.otava.library.documents.*;

public final class TestUtils {
    private TestUtils() {}

    public static LocalInMemoryTable createTable(String name, String alias) throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.getLocalTable("src/test/resources/tables/" + name, alias);
    }

    public static BasicDescriptor createDescriptor(String name, String alias) throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.getBasicDescriptor("src/test/resources/metadata/" + name, alias);
    }
}
