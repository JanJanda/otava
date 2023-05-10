package otava.library;

import java.io.FileNotFoundException;

final class DocumentFactory {
    public Table getLocalTable(String path) throws FileNotFoundException {
        return new LocalTable(path);
    }
}
