package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.CheckRunException;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import io.github.janjanda.otava.library.factories.CheckFactory;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

import static io.github.janjanda.otava.library.Manager.locale;
import static io.github.janjanda.otava.library.utils.DescriptorUtils.findColumnsWithDescriptions;
import static io.github.janjanda.otava.library.utils.DescriptorUtils.findTableDescriptionWithExc;

/**
 * This class checks required columns in tables. Cells of the required columns cannot be empty.
 */
public final class RequiredColumnsCheck extends Check {
    public RequiredColumnsCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(ColumnTitlesCheck.class));
    }

    @Override
    protected Result performValidation() throws CheckRunException, ValidatorFileException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
            JsonNode tableDescription = findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
            List<JsonNode> reqCols = findRequiredColumns(tableDescription);
            List<Integer> reqColIndices = findColumnsWithDescriptions(reqCols, table, this.getClass().getName());
            boolean columnsOk = reqColsNotEmpty(table, reqColIndices);
            if (!columnsOk) resultBuilder.setFatal().addMessage(locale().emptyRequiredColumn(table.getName()));
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

    private boolean reqColsNotEmpty(Table table, List<Integer> reqColIndices) {
        if (reqColIndices.isEmpty()) return true;
        for (CSVRecord row : table) {
            for (Integer i : reqColIndices) {
                if (row.get(i).isEmpty()) return false;
            }
        }
        return true;
    }
}
