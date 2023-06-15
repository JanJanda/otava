package otava.library.documents;

import otava.library.ValidatorException;
import java.io.InputStreamReader;

public interface Table extends Document {
    String getName();
    InputStreamReader getReader() throws ValidatorException;
}
