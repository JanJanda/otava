package otava.library.documents;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;

public class LocalDescriptor implements Descriptor {
    private final String fileName;
    private JsonNode root;

    public LocalDescriptor(String fileName) throws IOException {
        this.fileName = fileName;
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
    public JsonNode getRootNode() {
        return root;
    }

    @Override
    public JsonNode path(String fieldName) {
        return root.path(fieldName);
    }
}
