package io.github.janjanda.otava.library.documents;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static io.github.janjanda.otava.library.Manager.*;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import java.io.IOException;
import java.io.InputStreamReader;

public final class BasicDescriptor implements Descriptor {
    private final String fileName;
    private final String alias;
    private JsonNode root;

    public BasicDescriptor(String fileName, String alias, ReaderMaker readerMaker) throws ValidatorFileException {
        this.fileName = fileName;
        this.alias = alias;
        parseFile(readerMaker);
    }

    private void parseFile(ReaderMaker readerMaker) throws ValidatorFileException {
        try (InputStreamReader reader = readerMaker.makeReader(fileName)) {
            ObjectMapper om = new ObjectMapper();
            root = om.readTree(reader);
        }
        catch (IOException e) {
            throw new ValidatorFileException(locale().ioException(fileName) + " --- " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getPreferredName() {
        return alias == null ? fileName : alias;
    }

    @Override
    public JsonNode getRootNode() {
        return root;
    }

    @Override
    public JsonNode path(String fieldName) {
        return root.path(fieldName);
    }
}
