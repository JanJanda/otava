package otava.library;

import otava.library.documents.LocalInMemoryTable;

public final class DocumentFactory {
    public LocalInMemoryTable getLocalTable(String path) {
        return new LocalInMemoryTable(path);
    }
}
