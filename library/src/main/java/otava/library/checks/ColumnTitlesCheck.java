package otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import otava.library.exceptions.CheckCreationException;
import otava.library.*;
import otava.library.documents.*;

public final class ColumnTitlesCheck extends Check {
    public ColumnTitlesCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(TableDescriptorCheck.class));
    }

    @Override
    protected Result performValidation() {
        Result.Builder resultBuilder = new Result.Builder();
        if (fatalSubResult()) return resultBuilder.setSkipped().build();

        return null;
    }

    private JsonNode findTableDescription(Table table) {
        String tableName = table.getPreferredName();
        for (Descriptor desc : descriptors) {
            if (desc.path("url").asText().equals(tableName)) return desc.getRootNode();
        }
        return null;
    }
}
