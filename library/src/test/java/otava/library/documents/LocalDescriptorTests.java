package otava.library.documents;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalDescriptorTests {
    private Descriptor makeDescriptor() throws Exception {
        return new LocalDescriptor("src/test/resources/metadata/metadata001.json");
    }

    @Test
    void checkRoot() throws Exception {
        assertEquals("tree-ops.csv", makeDescriptor().getRootNode().path("url").asText());
    }

    @Test
    void checkPath() throws Exception {
        assertEquals("Tree Operations", makeDescriptor().path("dc:title").asText());
    }
}
