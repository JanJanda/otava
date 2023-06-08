package otava.library.documents;

import otava.library.ValidatorException;
import java.io.FileReader;

public interface Table extends Document {
    String getName();
    FileReader getReader() throws ValidatorException;
}
