package otava.library;

import org.apache.commons.csv.CSVFormat;
import otava.library.documents.*;
import otava.library.exceptions.ValidatorFileException;

public final class DocumentFactory {
    private final CSVFormat csvFormat= CSVFormat.Builder.create().setDelimiter(',').setQuote('"').setRecordSeparator('\n').setIgnoreEmptyLines(false).build();

    public LocalInMemoryTable getLocalTable(String path, String alias) throws ValidatorFileException {
        return new LocalInMemoryTable(path, csvFormat, alias);
    }

    public LocalDescriptor getLocalDescriptor(String path, String alias) throws ValidatorFileException {
        return new LocalDescriptor(path, alias);
    }
}
