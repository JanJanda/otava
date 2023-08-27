package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.factories.CheckFactory;
import java.util.List;
import static io.github.janjanda.otava.library.Manager.locale;
import static io.github.janjanda.otava.library.utils.DescriptorUtils.*;

/**
 * This class checks @types of entities in descriptors.
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_bfe3bea4a2e449c490bf4f0df6cd816c">Test 083: invalid tableGroup @type (and the following...)</a>
 */
public final class TypesCheck extends Check {
    public TypesCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(IdFormatCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Descriptor descriptor : descriptors) {
            if (descriptor.path("tables").isArray()) checkTypeNode(descriptor.path("@type"), "TableGroup", resultBuilder, descriptor.getName());
            checkTablesTypes(descriptor, resultBuilder);
            checkTypeNode(descriptor.path("dialect").path("@type"), "Dialect", resultBuilder, descriptor.getName());
            checkTransformsTypes(descriptor, resultBuilder);
        }
        return resultBuilder.build();
    }

    private void checkTablesTypes(Descriptor descriptor, Result.Builder resultBuilder) {
        List<JsonNode> tableDescs = extractTables(descriptor);
        for (JsonNode td : tableDescs) {
            checkTypeNode(td.path("@type"), "Table", resultBuilder, descriptor.getName());
            checkTypeNode(td.path("tableSchema").path("@type"), "Schema", resultBuilder, descriptor.getName());
            JsonNode cols = td.path("tableSchema").path("columns");
            if (cols.isArray()) {
                for (int i = 0; i < cols.size(); i++) {
                    checkTypeNode(cols.path(i).path("@type"), "Column", resultBuilder, descriptor.getName());
                }
            }
        }
    }

    private void checkTransformsTypes(Descriptor descriptor, Result.Builder resultBuilder) {
        JsonNode transforms = descriptor.path("transformations");
        if (transforms.isArray()) {
            for (int i = 0; i < transforms.size(); i++) {
                checkTypeNode(transforms.path(i).path("@type"), "Template", resultBuilder, descriptor.getName());
            }
        }
    }

    private void checkTypeNode(JsonNode typeNode, String expected, Result.Builder resultBuilder, String descName) {
        boolean badType = typeNode.isTextual() && !typeNode.asText().equals(expected);
        if (badType) resultBuilder.setFatal().addMessage(locale().badType(descName, expected));
    }
}
