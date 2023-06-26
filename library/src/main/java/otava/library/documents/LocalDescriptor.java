package otava.library.documents;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;

public final class LocalDescriptor implements Descriptor {
    private final String fileName;
    private final String alias;
    private JsonNode root;

    public LocalDescriptor(String fileName, String alias) throws IOException {
        this.fileName = fileName;
        this.alias = alias;
        parseFile();
    }

    private void parseFile() throws IOException {
        ObjectMapper om = new ObjectMapper();
        root = om.readTree(new FileInputStream(fileName));
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
    public JsonNode getRootNode() {
        return root;
    }

    @Override
    public JsonNode path(String fieldName) {
        return root.path(fieldName);
    }
}
