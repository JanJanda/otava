package otava.library;

import java.io.FileNotFoundException;
import java.io.FileReader;

final class LocalTable implements Table {
    private final FileReader fileReader;

    LocalTable(String path) throws FileNotFoundException {
        fileReader = new FileReader(path);
    }
}
