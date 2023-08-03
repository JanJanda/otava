package io.github.janjanda.otava.library;

import io.github.janjanda.otava.library.documents.*;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import io.github.janjanda.otava.library.utils.FileUtils;
import org.apache.commons.csv.CSVFormat;

public final class DocumentFactory {
    private final CSVFormat csvFormat = CSVFormat.Builder.create().setDelimiter(',').setQuote('"').setRecordSeparator('\n').setIgnoreEmptyLines(false).build();

    public BasicDescriptor makeLocalDescriptor(String path, String alias) throws ValidatorFileException {
        return new BasicDescriptor(path, alias, FileUtils::makeFileReader);
    }

    public BasicDescriptor makeOnlineDescriptor(String url, String alias) throws ValidatorFileException {
        return new BasicDescriptor(url, alias, FileUtils::makeUrlReader);
    }

    public InMemoryTable makeLocalInMemoryTable(String path, String alias) throws ValidatorFileException {
        return new InMemoryTable(path, alias, csvFormat, FileUtils::makeFileReader);
    }

    public InMemoryTable makeOnlineInMemoryTable(String url, String alias) throws ValidatorFileException {
        return new InMemoryTable(url, alias, csvFormat, FileUtils::makeUrlReader);
    }

    public RemoteTable makeLocalRemoteTable(String path, String alias) {
        return new RemoteTable(path, alias, csvFormat, FileUtils::makeFileReader);
    }

    public RemoteTable makeOnlineRemoteTable(String url, String alias) {
        return new RemoteTable(url, alias, csvFormat, FileUtils::makeUrlReader);
    }
}
