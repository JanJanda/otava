package otava.library;

import java.io.FileReader;

public interface Table extends Document {
    String getName();
    FileReader getReader() throws ValidatorException;
}
