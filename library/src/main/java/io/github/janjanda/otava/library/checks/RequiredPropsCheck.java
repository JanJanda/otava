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
 * This class checks the required properties in descriptors.
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_c0ca45f7c5012a39520cec7b44bc2668">Test 089: missing tables in TableGroup (and the following...)</a>
 */
public final class RequiredPropsCheck extends Check {
    public RequiredPropsCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(BaseUrlsCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Descriptor ds : descriptors) {
            boolean missingTables = ds.path("@type").isTextual() && ds.path("@type").asText().equals("TableGroup") && ds.path("tables").isMissingNode();
            if (missingTables) resultBuilder.setFatal().addMessage(locale().missingTablesArray(ds.getName()));
            checkTablesUrls(ds, resultBuilder);
        }
        return resultBuilder.build();
    }

    private void checkTablesUrls(Descriptor descriptor, Result.Builder resultBuilder) {
        List<JsonNode> tableDescs = extractTables(descriptor);
        for (JsonNode td : tableDescs) {
            if (!td.path("url").isTextual()) resultBuilder.setFatal().addMessage(locale().missingTableUrl(descriptor.getName()));
        }
    }
}
