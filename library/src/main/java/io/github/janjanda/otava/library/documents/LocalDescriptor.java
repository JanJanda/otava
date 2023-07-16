package io.github.janjanda.otava.library.documents;

import static io.github.janjanda.otava.library.utils.FileUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.janjanda.otava.library.Manager;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public final class LocalDescriptor implements Descriptor {
    private final String fileName;
    private final String alias;
    private JsonNode root;

    public LocalDescriptor(String fileName, String alias) throws ValidatorFileException {
        this.fileName = fileName;
        this.alias = alias;
        parseFile();
    }

    private void parseFile() throws ValidatorFileException {
        try (InputStreamReader reader = makeReader(fileName)) {
            ObjectMapper om = new ObjectMapper();
            root = om.readTree(reader);
        }
        catch (FileNotFoundException e) {
            throw new ValidatorFileException(Manager.locale().missingFile(fileName));
        }
        catch (IOException e) {
            throw new ValidatorFileException(Manager.locale().ioException());
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
