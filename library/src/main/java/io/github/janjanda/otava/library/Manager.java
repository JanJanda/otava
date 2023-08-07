package io.github.janjanda.otava.library;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.checks.FullRootCheck;
import io.github.janjanda.otava.library.documents.*;
import io.github.janjanda.otava.library.exceptions.*;
import io.github.janjanda.otava.library.locales.*;
import io.github.janjanda.otava.library.utils.DescriptorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Manager {
    private static Locale currentLocale = new EnglishLocale();

    public static Locale locale() {
        return currentLocale;
    }

    public Manager() {}

    public Manager(Locale locale) {
        currentLocale = locale;
    }

    /**
     * Validates provided tables together with their respective descriptors.
     * @param tableFileNames Names of the tables in the filesystem
     * @param tableAliases Aliases of the respective tables - {@code tableAliases[i]} belongs to {@code tableFileNames[i]} so these arrays must have the same length. This array cannot be {@code null}, but it can contain {@code null} if a particular alias is not necessary.
     * @param descriptorFileNames Names of the descriptors in the filesystem.
     * @param descriptorAliases Same as the parameter {@code tableFileNames}, but this one is for descriptors.
     * @return Set of validation results. One result for each validation check.
     * @throws ValidatorException If the validation cannot be performed.
     */
    public Set<Result> manualLocalValidation(String[] tableFileNames, String[] tableAliases,
                                              String[] descriptorFileNames, String[] descriptorAliases) throws ValidatorException {
        DocumentFactory documentFactory = new DocumentFactory();
        Table[] tables = new Table[tableFileNames.length];
        for (int i = 0; i < tableFileNames.length; i++) {
            tables[i] = documentFactory.makeLocalInMemoryTable(tableFileNames[i], tableAliases[i]);
        }
        Descriptor[] descriptors = new Descriptor[descriptorFileNames.length];
        for (int i = 0; i < descriptorFileNames.length; i++) {
            descriptors[i] = documentFactory.makeLocalDescriptor(descriptorFileNames[i], descriptorAliases[i]);
        }
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descriptors));
        FullRootCheck rootCheck = scf.getInstance(FullRootCheck.class);
        rootCheck.validate();
        return rootCheck.getAllResults();
    }

    public Result[] fullValidation(ValidationSuite validationSuite) throws ValidatorException {
        return null;
    }

    public String printFullValidationTree() throws CheckCreationException {
        SingletonCheckFactory scf = new SingletonCheckFactory(null, null);
        FullRootCheck rootCheck = scf.getInstance(FullRootCheck.class);
        return rootCheck.printTree();
    }
}
