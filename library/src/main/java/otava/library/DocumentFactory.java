package otava.library;

public final class DocumentFactory {
    public LocalTable getLocalTable(String path) {
        return new LocalTable(path);
    }
}
