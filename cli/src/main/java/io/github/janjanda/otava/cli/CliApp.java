package io.github.janjanda.otava.cli;

import io.github.janjanda.otava.library.Manager;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.exceptions.ValidatorException;
import java.io.IOException;
import java.util.Set;

public class CliApp {
    public static void main(String[] args) throws IOException {
        Manager m = new Manager();
        String[] tables = {"library/src/test/resources/tables/table004.csv"};
        String[] tableAlias = {"https://example.org/tree-ops.csv"};
        String[] descriptors = {"library/src/test/resources/metadata/metadata001.json"};
        String[] descAlias = {"https://example.org/metadata001.json"};
        try {
            Set<Result> results = m.manualLocalValidation(tables, tableAlias, descriptors, descAlias);
            for (Result result : results) {
                System.out.println(result.asText());
            }
        }
        catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }
    }
}
