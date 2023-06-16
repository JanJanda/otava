package otava.library.documents;

import otava.library.ValidatorException;
import java.io.InputStreamReader;

public interface Table extends Document {
    String getName();
    int getWidth();
    int getHeight();
    String getCell(int row, int column);
    InputStreamReader getReader() throws ValidatorException;
}
