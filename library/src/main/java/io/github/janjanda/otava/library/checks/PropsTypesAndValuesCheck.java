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
 * This class checks actual types and values of properties in descriptors.
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_dbe6c840800258cb99907ca1ba09dc2b">Test 074: empty tables</a>
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_053758a890cf73e2866e16e23887e6c5">Test 100: inconsistent array values: columns</a>
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_b931442cec955794ecd8de971e026c81">Test 107: invalid tableSchema</a>
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_17b0d40d1a200632b4fe49c41131f2ba">Test 111: titles with invalid value</a>
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_81724b3ccf5dd39130c569ee2a6e2b4e">Test 135: @list value</a>
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_01bc25e31e6db6e621d317e73a7da77d">Test 136: @set value</a>
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_49dd48645c18ca607fa8fa09801f48d7">Test 137: @type out of range (as datatype)</a>
 */
public final class PropsTypesAndValuesCheck extends Check {
    public PropsTypesAndValuesCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(RequiredPropsCheck.class));
    }

    @Override
    protected Result.Builder performValidation() {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped();
        for (Descriptor descriptor : descriptors) {
            JsonNode tablesArray = descriptor.path("tables");
            if (tablesArray.isArray() && tablesArray.size() == 0) resultBuilder.setFatal().addMessage(locale().emptyTablesArray(descriptor.getName()));
            checkColsArray(descriptor, resultBuilder);
            checkListsAndSets(descriptor, resultBuilder);
            checkTypesAndIds(descriptor, resultBuilder);
        }
        return resultBuilder;
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

    private void checkListsAndSets(Descriptor descriptor, Result.Builder resultBuilder) {
        JsonNode listNode = descriptor.getRootNode().findValue("@list");
        if (listNode != null) resultBuilder.setFatal().addMessage(locale().forbiddenValue("@list", descriptor.getName()));
        JsonNode setNode = descriptor.getRootNode().findValue("@set");
        if (setNode != null) resultBuilder.setFatal().addMessage(locale().forbiddenValue("@set", descriptor.getName()));
    }

    private void checkTypesAndIds(Descriptor descriptor, Result.Builder resultBuilder) {
        checkNonBlankString(descriptor, resultBuilder, "@type");
        checkNonBlankString(descriptor, resultBuilder, "@id");
    }

    private void checkNonBlankString(Descriptor descriptor, Result.Builder resultBuilder, String propName) {
        List<JsonNode> typeNodes = descriptor.getRootNode().findValues(propName);
        if (hasBlankNode(typeNodes)) resultBuilder.setFatal().addMessage(locale().noBlanks(propName, descriptor.getName()));
        if (hasNonString(typeNodes)) resultBuilder.setFatal().addMessage(locale().propBadType(propName, descriptor.getName()));
    }

    private boolean hasBlankNode(List<JsonNode> nodes) {
        for (JsonNode node : nodes) {
            if (node.isTextual() && node.asText().startsWith("_:")) return true;
        }
        return false;
    }

    private boolean hasNonString(List<JsonNode> nodes) {
        for (JsonNode node : nodes) {
            if (!node.isTextual()) return true;
        }
        return false;
    }
}
