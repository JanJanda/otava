package io.github.janjanda.otava.library.checks;

import static io.github.janjanda.otava.library.Manager.*;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.ValidatorException;
import io.github.janjanda.otava.library.factories.CheckFactory;

/**
 * This class checks the length of the {@code tables} array in descriptors.
 * @see <a href="https://w3c.github.io/csvw/tests/reports/index.html#test_dbe6c840800258cb99907ca1ba09dc2b">Test 074: empty tables</a>
 */
public final class TablesArrayCheck extends Check {
    public TablesArrayCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(ContextCheck.class));
    }

    @Override
    protected Result performValidation() throws ValidatorException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Descriptor descriptor : descriptors) {
            JsonNode tablesArray = descriptor.path("tables");
            if (tablesArray.isArray() && tablesArray.size() == 0) resultBuilder.setFatal().addMessage(locale().emptyTablesArray(descriptor.getName()));
        }
        return resultBuilder.build();
    }
}
