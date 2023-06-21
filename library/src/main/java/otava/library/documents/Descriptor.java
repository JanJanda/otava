package otava.library.documents;

import com.fasterxml.jackson.databind.JsonNode;

public interface Descriptor extends Document {
    JsonNode getRootNode();
    JsonNode path(String fieldName);
}
