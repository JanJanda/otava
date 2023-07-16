package io.github.janjanda.otava.library.documents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class LocalDescriptorTests {
    private Descriptor makeDescriptor() throws Exception {
        return new LocalDescriptor("src/test/resources/metadata/metadata001.json", null);
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
