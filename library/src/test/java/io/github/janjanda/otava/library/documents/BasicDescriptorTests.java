package io.github.janjanda.otava.library.documents;

import static io.github.janjanda.otava.library.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class BasicDescriptorTests {
    @Test
    void checkRoot() throws Exception {
        assertEquals("tree-ops.csv", createDescriptor("metadata001.json", null).getRootNode().path("url").asText());
    }

    @Test
    void checkPath() throws Exception {
        assertEquals("Tree Operations", createDescriptor("metadata001.json", null).path("dc:title").asText());
    }
}
