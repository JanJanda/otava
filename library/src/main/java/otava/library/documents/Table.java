package otava.library.documents;

import otava.library.exceptions.ValidatorFileException;
import java.io.InputStreamReader;

public interface Table extends Document {
    int getWidth();
    int getHeight();
    String getCell(int row, int column);
    InputStreamReader getReader() throws ValidatorFileException;
}
