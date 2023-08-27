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
 * This class checks actual types of properties in descriptors.
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_dbe6c840800258cb99907ca1ba09dc2b">Test 074: empty tables</a>
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_053758a890cf73e2866e16e23887e6c5">Test 100: inconsistent array values: columns</a>
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_b931442cec955794ecd8de971e026c81">Test 107: invalid tableSchema</a>
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_17b0d40d1a200632b4fe49c41131f2ba">Test 111: titles with invalid value</a>
 */
public final class PropsTypesCheck extends Check {
    public PropsTypesCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(RequiredPropsCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Descriptor descriptor : descriptors) {
            JsonNode tablesArray = descriptor.path("tables");
            if (tablesArray.isArray() && tablesArray.size() == 0) resultBuilder.setFatal().addMessage(locale().emptyTablesArray(descriptor.getName()));
            checkColsArray(descriptor, resultBuilder);
        }
        return resultBuilder.build();
    }

    private void checkColsArray(Descriptor descriptor, Result.Builder resultBuilder) {
        List<JsonNode> tableDescs = extractTables(descriptor);
        for (JsonNode td : tableDescs) {
            if (!td.path("tableSchema").isMissingNode() && !td.path("tableSchema").isObject()) resultBuilder.setFatal().addMessage(locale().propIsNotObject("tableSchema", descriptor.getName()));
            JsonNode colsNode = td.path("tableSchema").path("columns");
            if (!colsNode.isMissingNode() && !colsNode.isArray()) resultBuilder.setFatal().addMessage(locale().propIsNotArray("columns", descriptor.getName()));
            if (colsNode.isArray()) {
                for (int i = 0; i < colsNode.size(); i++) {
                    JsonNode titlesNode = colsNode.path(i).path("titles");
                    if (!titlesNode.isMissingNode() && !titlesNode.isTextual() && !titlesNode.isArray() && !titlesNode.isObject()) {
                        resultBuilder.setFatal().addMessage(locale().propBadType("titles", descriptor.getName()));
                    }
                }
            }
        }
    }
}
