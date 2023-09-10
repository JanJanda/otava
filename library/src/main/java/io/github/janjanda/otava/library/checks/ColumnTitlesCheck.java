package io.github.janjanda.otava.library.checks;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.CheckRunException;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import io.github.janjanda.otava.library.factories.CheckFactory;
import org.apache.commons.csv.CSVRecord;

import java.util.List;

import static io.github.janjanda.otava.library.Manager.locale;
import static io.github.janjanda.otava.library.utils.DescriptorUtils.*;

/**
 * This class checks columns in tables and their respective descriptors.
 * There should be columns with same titles in a table and its descriptor.
 */
public final class ColumnTitlesCheck extends Check {
    public ColumnTitlesCheck(CheckFactory f) throws CheckCreationException {
        super(f.getTables(), f.getDescriptors(), f.getInstance(TableDescriptorCheck.class));
    }

    @Override
    protected Result.Builder performValidation() throws CheckRunException, ValidatorFileException {
        Result.Builder resultBuilder = new Result.Builder(this.getClass().getName());
        if (fatalSubResult()) return resultBuilder.setSkipped();
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
                if (indexFound == -1) resultBuilder.setFatal().addMessage(locale().missingColDesc(tableColName, table.getName()));
                else descColumns.remove(indexFound);
            }
            for (JsonNode missedCol : descColumns) {
                resultBuilder.setFatal().addMessage(locale().missingCol(missedCol.path("name").asText(), tableDescription.path("url").asText()));
            }
        }
        return resultBuilder;
    }
}
