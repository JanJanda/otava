package io.github.janjanda.otava.library;

import io.github.janjanda.otava.library.documents.LocalDescriptor;
import io.github.janjanda.otava.library.documents.LocalInMemoryTable;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import org.apache.commons.csv.CSVFormat;

public final class DocumentFactory {
    private final CSVFormat csvFormat= CSVFormat.Builder.create().setDelimiter(',').setQuote('"').setRecordSeparator('\n').setIgnoreEmptyLines(false).build();

    public LocalInMemoryTable getLocalTable(String path, String alias) throws ValidatorFileException {
        return new LocalInMemoryTable(path, csvFormat, alias);
    }

    public LocalDescriptor getLocalDescriptor(String path, String alias) throws ValidatorFileException {
        return new LocalDescriptor(path, alias);
    }
}
