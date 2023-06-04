package otava.library;

import java.io.FileReader;

public interface Table extends Document {
    String getName();
    FileReader getFileReader() throws ValidatorException;
}
