package otava.library.documents;

import static otava.library.utils.FileUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStreamReader;

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
        try (InputStreamReader reader = makeReader(fileName)) {
            ObjectMapper om = new ObjectMapper();
            root = om.readTree(reader);
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
