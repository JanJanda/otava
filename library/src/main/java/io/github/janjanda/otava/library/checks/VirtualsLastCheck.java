package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.factories.CheckFactory;

import java.util.List;

import static io.github.janjanda.otava.library.Manager.locale;
import static io.github.janjanda.otava.library.utils.DescriptorUtils.extractTables;

/**
 * This class checks the order of virtual and non-virtual columns in table descriptions.
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_26a81e8db71726b5386f0338571784bd">Test 133: virtual before non-virtual</a>
 */
public final class VirtualsLastCheck extends Check {
    public VirtualsLastCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(TypesCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Descriptor descriptor : descriptors) {
            List<JsonNode> tableDescs = extractTables(descriptor);
            for (JsonNode td : tableDescs) {
                JsonNode colsNode = td.path("tableSchema").path("columns");
                if (colsNode.isArray()) checkColsArray(colsNode, resultBuilder, descriptor.getName());
            }
        }
        return resultBuilder.build();
    }

    private void checkColsArray(JsonNode colsArray, Result.Builder resultBuilder, String descName) {
        boolean virtualFound = false;
        for (int i = 0; i < colsArray.size(); i++) {
            if (columnIsVirtual(colsArray.path(i))) virtualFound = true;
            else if (virtualFound) resultBuilder.setFatal().addMessage(locale().virtBeforeNonVirt(descName));
        }
    }

    private boolean columnIsVirtual(JsonNode column) {
        JsonNode virtNode = column.path("virtual");
        return virtNode.isBoolean() && virtNode.asBoolean();
    }
}
