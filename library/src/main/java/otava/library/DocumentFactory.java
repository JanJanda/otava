package otava.library;

import org.apache.commons.csv.CSVFormat;
import otava.library.documents.*;
import java.io.IOException;

public final class DocumentFactory {
    private final CSVFormat csvFormat= CSVFormat.Builder.create().setDelimiter(',').setQuote('"').setRecordSeparator('\n').setIgnoreEmptyLines(false).build();

    public LocalInMemoryTable getLocalTable(String path, String alias) throws IOException {
        return new LocalInMemoryTable(path, csvFormat, alias);
    }

    public LocalDescriptor getLocalDescriptor(String path) throws IOException {
        return new LocalDescriptor(path, null);
    }
}
