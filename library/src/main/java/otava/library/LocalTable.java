package otava.library;

import java.io.FileNotFoundException;
import java.io.FileReader;

public final class LocalTable implements Table {
    private final String fileName;

    LocalTable(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public FileReader getReader() throws ValidatorException {
        try {
            return new FileReader(fileName);
        }
        catch (FileNotFoundException e) {
            throw new ValidatorException("Unexpected FileNotFoundException.");
        }
    }
}
