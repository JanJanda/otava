package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.CheckFactory;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.CheckRunException;
import io.github.janjanda.otava.library.utils.DescriptorUtils;
import java.util.List;

public final class DatatypesCheck extends Check {
    public DatatypesCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(ColumnTitlesCheck.class));
    }

    @Override
    protected Result performValidation() throws CheckRunException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        for (Table table : tables) {
            JsonNode tableDescription = DescriptorUtils.findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
            List<JsonNode> columns = DescriptorUtils.extractNonVirtualColumns(tableDescription.path("tableSchema"));
        }

        return resultBuilder.build();
    }
}
