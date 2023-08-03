package io.github.janjanda.otava.library;

import io.github.janjanda.otava.library.documents.*;

public final class TestUtils {
    private TestUtils() {}

    public static InMemoryTable createTable(String name, String alias) throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.makeLocalInMemoryTable("src/test/resources/tables/" + name, alias);
    }

    public static BasicDescriptor createDescriptor(String name, String alias) throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.makeLocalDescriptor("src/test/resources/metadata/" + name, alias);
    }
}
