package otava.library.checks;

import static otava.library.utils.DescriptorUtils.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.csv.CSVRecord;
import otava.library.*;
import otava.library.documents.Table;
import otava.library.exceptions.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class checks required columns in tables. Cells of the required columns cannot be empty.
 */
public final class RequiredColumnsCheck extends Check {
    public RequiredColumnsCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(ColumnTitlesCheck.class));
    }

    @Override
    protected Result performValidation() throws CheckRunException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
            JsonNode tableDescription = findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
            List<JsonNode> reqCols = findRequiredColumns(tableDescription);
            List<Integer> reqColIndices = getReqColIndices(table, reqCols);
            boolean columnsOk = reqColsNotEmpty(table, reqColIndices);
            if (!columnsOk) resultBuilder.setFatal().addMessage(Manager.locale().emptyRequiredColumn(table.getName()));
        }
        return resultBuilder.build();
    }

    private List<JsonNode> findRequiredColumns(JsonNode tableDescription) {
        List<JsonNode> reqCols = new ArrayList<>();
        JsonNode colsDesc = tableDescription.path("tableSchema").path("columns");
        if (colsDesc.isArray()) {
            for (int i = 0; i < colsDesc.size(); i++) {
                JsonNode col = colsDesc.path(i);
                JsonNode reqValue = col.path("required");
                JsonNode virtualValue = col.path("virtual");
                boolean isRequired = reqValue.isBoolean() && reqValue.asBoolean();
                boolean isVirtual = virtualValue.isBoolean() && virtualValue.asBoolean();
                if (isRequired && !isVirtual) reqCols.add(col);
            }
        }
        return reqCols;
    }

    private List<Integer> getReqColIndices(Table table, List<JsonNode> reqCols) throws CheckRunException {
        List<Integer> reqColInds = new ArrayList<>();
        CSVRecord firstLine = table.getFirstLine();
        for (JsonNode reqCol : reqCols) {
            int colIndex = findColumnWithTitle(firstLine, reqCol.path("titles"));
            if (colIndex == -1) throw new CheckRunException(this.getClass().getName());
            else reqColInds.add(colIndex);
        }
        return reqColInds;
    }

    private boolean reqColsNotEmpty(Table table, List<Integer> reqColIndices) {
        if (reqColIndices.isEmpty()) return true;
        for (CSVRecord row : table) {
            for (Integer i : reqColIndices) {
                if (row.get(i).equals("")) return false;
            }
        }
        return true;
    }
}
