package otava.library;

import org.apache.commons.csv.CSVFormat;
import otava.library.documents.LocalInMemoryTable;
import java.io.IOException;

public final class DocumentFactory {
    private final CSVFormat csvFormat= CSVFormat.Builder.create().setDelimiter(',').setQuote('"').setRecordSeparator('\n').setIgnoreEmptyLines(false).build();

    public LocalInMemoryTable getLocalTable(String path) throws IOException {
        return new LocalInMemoryTable(path, csvFormat);
    }
}
