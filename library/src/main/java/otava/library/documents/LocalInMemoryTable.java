package otava.library.documents;

import otava.library.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class LocalInMemoryTable implements Table {
    private final String fileName;

    public LocalInMemoryTable(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public InputStreamReader getReader() throws ValidatorException {
        try {
            return new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
        }
        catch (FileNotFoundException e) {
            throw new ValidatorException(Manager.locale().missingFile(fileName));
        }
    }
}
