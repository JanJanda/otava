package otava.library;

import otava.library.checks.*;
import otava.library.documents.*;
import otava.library.exceptions.ValidatorException;
import otava.library.locales.*;
import java.util.Set;

public final class Manager {
    private static Locale currentLocale = new EnglishLocale();

    public Manager() {}

    public Manager(Locale locale) {
        currentLocale = locale;
    }

    public static Locale locale() {
        return currentLocale;
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
            tables[i] = documentFactory.getLocalTable(tableFileNames[i], tableAliases[i]);
        }
        Descriptor[] descriptors = new Descriptor[descriptorFileNames.length];
        for (int i = 0; i < descriptorFileNames.length; i++) {
            descriptors[i] = documentFactory.getLocalDescriptor(descriptorFileNames[i], descriptorAliases[i]);
        }
        SingletonCheckFactory scf = new SingletonCheckFactory(new DocsGroup<>(tables), new DocsGroup<>(descriptors));
        RootCheck rootCheck = scf.getInstance(RootCheck.class);
        rootCheck.validate();
        return rootCheck.getAllResults();
    }
}
