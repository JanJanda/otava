package io.github.janjanda.otava.library;

import io.github.janjanda.otava.library.documents.*;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import io.github.janjanda.otava.library.utils.FileUtils;
import org.apache.commons.csv.CSVFormat;

public final class DocumentFactory {
    private final CSVFormat csvFormat = CSVFormat.Builder.create().setDelimiter(',').setQuote('"').setRecordSeparator('\n').setIgnoreEmptyLines(false).build();

    public InMemoryTable getLocalTable(String path, String alias) throws ValidatorFileException {
        return new InMemoryTable(path, alias, csvFormat, FileUtils::makeFileReader);
    }

    public BasicDescriptor getLocalDescriptor(String path, String alias) throws ValidatorFileException {
        return new BasicDescriptor(path, alias, FileUtils::makeFileReader);
    }
}
