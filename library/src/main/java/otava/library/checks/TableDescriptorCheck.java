package otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import otava.library.CheckFactory;
import otava.library.Manager;
import otava.library.Result;
import otava.library.ValidatorException;
import otava.library.documents.Descriptor;
import otava.library.documents.Table;

public final class TableDescriptorCheck extends Check {
    public TableDescriptorCheck(CheckFactory f) throws ValidatorException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(LineBreaksCheck.class), f.getInstance(ContextCheck.class));
    }

    @Override
    protected Result performValidation() throws ValidatorException {
        Result.Builder resultBuilder = new Result.Builder();
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
            if (!descriptorForTableFound(table)) resultBuilder.addMessage(Manager.locale().missingDesc(table.getName()));
        }

        return null;
    }

    private boolean descriptorForTableFound(Table table) {
        String tableName = table.getName();
        if (table.getAlias() != null) tableName = table.getAlias();
        for (Descriptor desc : descriptors) {
            if (desc.path("url").asText().equals(tableName)) return true;
            JsonNode tablesArray = desc.path("tables");
            if (tablesArray.isArray()) {
                for (int i = 0; i < tablesArray.size(); i++) {
                    if (tablesArray.path(i).path("url").asText().equals(tableName)) return true;
                }
            }
        }
        return false;
    }

    private boolean tableForDescriptorFound(Descriptor descriptor) {

        return false;
    }
}
