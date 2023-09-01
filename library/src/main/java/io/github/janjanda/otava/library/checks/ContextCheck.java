package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.factories.CheckFactory;

import java.util.List;

import static io.github.janjanda.otava.library.Manager.locale;

/**
 * This class checks the @context fields of the metadata descriptors according to the corresponding W3C Recommendation.
 * @see <a href="https://www.w3.org/TR/2015/REC-tabular-metadata-20151217/#top-level-properties">Metadata Vocabulary for Tabular Data</a>
 */
public final class ContextCheck extends Check {
    public ContextCheck(CheckFactory f) {
        super(f.getTables(), f.getDescriptors());
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        for (Descriptor descriptor : descriptors) {
            if (!correctContext(descriptor)) resultBuilder.addMessage(locale().badContext(descriptor.getName())).setFatal();
            if (someOtherContext(descriptor)) resultBuilder.addMessage(locale().extraContext(descriptor.getName())).setFatal();
        }
        return resultBuilder.build();
    }

    private boolean correctContext(Descriptor descriptor) {
        JsonNode contextNode = descriptor.path("@context");
        boolean isSimpleContext = contextNode.asText().equals("http://www.w3.org/ns/csvw");
        boolean isComplexContext = contextNode.isArray() &&
                contextNode.size() == 2 &&
                contextNode.path(0).asText().equals("http://www.w3.org/ns/csvw") &&
                contextNode.path(1).isObject();
        JsonNode objectItem = contextNode.path(1);
        boolean correctObject = (objectItem.size() == 2 && objectItem.path("@base").isTextual() && objectItem.path("@language").isTextual()) ||
                (objectItem.size() == 1 && (objectItem.path("@base").isTextual() || objectItem.path("@language").isTextual()));
        return isSimpleContext || (isComplexContext && correctObject);
    }

    private boolean someOtherContext(Descriptor descriptor) {
        List<JsonNode> foundContexts = descriptor.getRootNode().findValues("@context");
        return foundContexts.size() > 1;
    }
}
