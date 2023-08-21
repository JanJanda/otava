package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.*;
import static io.github.janjanda.otava.library.Manager.*;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.*;
import static io.github.janjanda.otava.library.utils.DescriptorUtils.*;

import io.github.janjanda.otava.library.factories.CheckFactory;
import org.apache.commons.csv.CSVRecord;
import java.util.List;

/**
 * This class checks columns in tables and their respective descriptors.
 * There should be columns with same titles in a table and its descriptor.
 */
public final class ColumnTitlesCheck extends Check {
    public ColumnTitlesCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(TableDescriptorCheck.class));
    }

    @Override
    protected Result performValidation() throws CheckRunException, ValidatorFileException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped().build();
        for (Table table : tables) {
            JsonNode tableDescription = findTableDescriptionWithExc(table, descriptors, this.getClass().getName());
            List<JsonNode> descColumns = extractNonVirtualColumns(tableDescription.path("tableSchema"));
            CSVRecord firstLine = table.getFirstLine();
            for (String tableColName : firstLine) {
                int indexFound = -1;
                for (int i = 0; i < descColumns.size(); i++) {
                    JsonNode colDesc = descColumns.get(i);
                    if (isStringInName(tableColName, colDesc.path("name")) || isStringInTitle(tableColName, colDesc.path("titles"))) indexFound = i;
                }
                if (indexFound == -1) resultBuilder.addMessage(locale().missingColDesc(tableColName, table.getName()));
                else descColumns.remove(indexFound);
            }
            for (JsonNode missedCol : descColumns) {
                resultBuilder.setFatal().addMessage(locale().missingCol(missedCol.path("name").asText(), tableDescription.path("url").asText()));
            }
        }
        return resultBuilder.build();
    }
}
