package io.github.janjanda.otava.library.checks;

import static io.github.janjanda.otava.library.Manager.*;
import static io.github.janjanda.otava.library.utils.DescriptorUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.factories.CheckFactory;
import java.util.List;

/**
 * This class checks the format of some IDs in descriptors.
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_d5cd1a52318dc295dfeb575845dfc1b7">Test 077: invalid tableGroup @id [and the following...]</a>
 */
public final class IdFormatCheck extends Check {
    public IdFormatCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(TablesArrayCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Descriptor descriptor : descriptors) {
            checkIdNode(descriptor.path("@id"), resultBuilder, descriptor.getName());
            checkIdNode(descriptor.path("tableSchema").path("@id"), resultBuilder, descriptor.getName());
            checkColumnsIds(descriptor, resultBuilder);
            checkIdNode(descriptor.path("dialect").path("@id"), resultBuilder, descriptor.getName());
            checkTransformsIds(descriptor, resultBuilder);
        }
        return resultBuilder.build();
    }

    private void checkColumnsIds(Descriptor descriptor, Result.Builder resultBuilder) {
        List<JsonNode> tableDescriptions = extractTables(descriptor);
        for (JsonNode td : tableDescriptions) {
            JsonNode cols = td.path("tableSchema").path("columns");
            if (cols.isArray()) {
                for (int i = 0; i < cols.size(); i++) {
                    checkIdNode(cols.path(i).path("@id"), resultBuilder, descriptor.getName());
                }
            }
        }
    }

    private void checkTransformsIds(Descriptor descriptor, Result.Builder resultBuilder) {
        JsonNode transforms = descriptor.path("transformations");
        if (transforms.isArray()) {
            for (int i = 0; i < transforms.size(); i++) {
                checkIdNode(transforms.path(i).path("@id"), resultBuilder, descriptor.getName());
            }
        }
    }

    private void checkIdNode(JsonNode idNode, Result.Builder resultBuilder, String descName) {
        boolean badId = idNode.isTextual() && idNode.asText().length() >= 2 && idNode.asText().substring(0, 2).equals("_:");
        if (badId) resultBuilder.setFatal().addMessage(locale().invalidId(descName));
    }
}
