package otava.library;

import otava.library.documents.LocalTable;

public final class DocumentFactory {
    public LocalTable getLocalTable(String path) {
        return new LocalTable(path);
    }
}
