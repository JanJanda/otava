package io.github.janjanda.otava.library;

import io.github.janjanda.otava.library.documents.LocalDescriptor;
import io.github.janjanda.otava.library.documents.LocalInMemoryTable;

public final class TestUtils {
    private TestUtils() {}

    public static LocalInMemoryTable createTable(String name, String alias) throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.getLocalTable("src/test/resources/tables/" + name, alias);
    }

    public static LocalDescriptor createDescriptor(String name, String alias) throws Exception {
        DocumentFactory df = new DocumentFactory();
        return df.getLocalDescriptor("src/test/resources/metadata/" + name, alias);
    }
}