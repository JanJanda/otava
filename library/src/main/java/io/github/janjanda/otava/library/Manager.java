package io.github.janjanda.otava.library;

import static io.github.janjanda.otava.library.ValidationSuite.*;
import static io.github.janjanda.otava.library.utils.DescriptorUtils.*;
import static io.github.janjanda.otava.library.utils.UrlUtils.*;
import io.github.janjanda.otava.library.checks.*;
import io.github.janjanda.otava.library.documents.*;
import io.github.janjanda.otava.library.exceptions.*;
import io.github.janjanda.otava.library.locales.*;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Manager {
    private static Locale currentLocale = new EnglishLocale();

    /**
     * Gets the value of the static variable with the current locale. The locale is in a static variable, and it affects the entire program.
     * @return current locale
     */
    public static Locale locale() {
        return currentLocale;
    }

    public Manager() {}

    /**
     * Creates an instance of the class and sets the locale. The locale is in a static variable, and it affects the entire program.
     * @param locale an instance of a locale
     */
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
    @Deprecated
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

    /**
     * Validates only tables without descriptors. Only passive tables from the validation suite are validated.
     * @param validationSuite specification of validated files, only passive tables are read
     * @return results of individual validation checks
     * @throws ValidatorException If the validation cannot be performed.
     */
    public Result[] tablesOnlyValidation(ValidationSuite validationSuite) throws ValidatorException {
        DocumentFactory df = new DocumentFactory();
        List<Table> tables = createTables(validationSuite.getPassiveTables(), df);
        DocsGroup<Table> tableGroup = new DocsGroup<>(tables.toArray(new Table[0]));
        SingletonCheckFactory scf = new SingletonCheckFactory(tableGroup, null);
        ConsistentColumnsCheck ccc = scf.getInstance(ConsistentColumnsCheck.class);
        ccc.validate();
        Set<Result> results = ccc.getAllResults();
        return results.toArray(new Result[0]);
    }

    /**
     * Validate only descriptors without tables. Only passive descriptors from the validation suite are validated.
     * @param validationSuite specification of validate files, only passive descriptors are read
     * @return results of individual validation checks
     * @throws ValidatorException If the validation cannot be performed.
     */
    public Result[] descriptorsOnlyValidation(ValidationSuite validationSuite) throws ValidatorException {
        DocumentFactory df = new DocumentFactory();
        List<Descriptor> descriptors = createDescriptors(validationSuite.getPassiveDescriptors(), df);
        DocsGroup<Descriptor> descGroup = new DocsGroup<>(descriptors.toArray(new Descriptor[0]));
        SingletonCheckFactory scf = new SingletonCheckFactory(null, descGroup);
        ContextCheck cc = scf.getInstance(ContextCheck.class);
        cc.validate();
        Set<Result> results = cc.getAllResults();
        return results.toArray(new Result[0]);
    }

    /**
     * Performs full validation with the provided validation suite.
     * @param validationSuite specifications of validated files
     * @return results of individual validation checks
     * @throws ValidatorException If the validation cannot be performed.
     */
    public Result[] fullValidation(ValidationSuite validationSuite) throws ValidatorException {
        DocumentFactory df = new DocumentFactory();
        List<Table> tables = createTables(validationSuite.getActiveTables(), df);
        List<Descriptor> descriptors = createDescriptors(validationSuite.getActiveDescriptors(), df);
        DescResource[] descResources = createDescResources(tables);
        TableResource[] tableResources = createTableResources(descriptors);
        tables.addAll(createTables(tableResources, df));
        descriptors.addAll(createDescriptors(descResources, df));
        tables.addAll(createTables(validationSuite.getPassiveTables(), df));
        descriptors.addAll(createDescriptors(validationSuite.getPassiveDescriptors(), df));
        DocsGroup<Table> tableGroup = new DocsGroup<>(tables.toArray(new Table[0]));
        DocsGroup<Descriptor> descGroup = new DocsGroup<>(descriptors.toArray(new Descriptor[0]));
        SingletonCheckFactory scf = new SingletonCheckFactory(tableGroup, descGroup);
        FullRootCheck rootCheck = scf.getInstance(FullRootCheck.class);
        rootCheck.validate();
        Set<Result> results = rootCheck.getAllResults();
        return results.toArray(new Result[0]);
    }

    private List<Table> createTables(TableResource[] specs, DocumentFactory df) throws ValidatorFileException {
        List<Table> tables = new ArrayList<>();
        for (TableResource spec : specs) {
            if (spec.isLocal()) {
                if (spec.outOfMemory()) tables.add(df.makeLocalRemoteTable(spec.name(), spec.alias()));
                else tables.add(df.makeLocalInMemoryTable(spec.name(), spec.alias()));
            }
            else {
                if (spec.outOfMemory()) tables.add(df.makeOnlineRemoteTable(spec.name(), spec.alias()));
                else tables.add(df.makeOnlineInMemoryTable(spec.name(), spec.alias()));
            }
        }
        return tables;
    }

    private List<Descriptor> createDescriptors(DescResource[] specs, DocumentFactory df) throws ValidatorFileException {
        List<Descriptor> descriptors = new ArrayList<>();
        for (DescResource spec : specs) {
            if (spec.isLocal()) descriptors.add(df.makeLocalDescriptor(spec.name(), spec.alias()));
            else descriptors.add(df.makeOnlineDescriptor(spec.name(), spec.alias()));
        }
        return descriptors;
    }

    private DescResource[] createDescResources(List<Table> activeTables) {
        List<DescResource> resources = new ArrayList<>();
        for (Table activeTable : activeTables) {
            resources.add(new DescResource(activeTable.getPreferredName() + "-metadata.json", null, false));
        }
        return resources.toArray(new DescResource[0]);
    }

    private TableResource[] createTableResources(List<Descriptor> activeDescs) throws ValidatorFileException {
        List<TableResource> resources = new ArrayList<>();
        for (Descriptor activeDesc : activeDescs) {
            String baseUrl = getBaseUrl(activeDesc);
            List<JsonNode> extractedTables = extractTables(activeDesc);
            for (JsonNode extractedTable : extractedTables) {
                JsonNode urlNode = extractedTable.path("url");
                if (urlNode.isTextual()) {
                    try {
                        String url = resolveUrl(baseUrl, urlNode.asText());
                        resources.add(new TableResource(url, null, false, true));
                    }
                    catch (MalformedURLException e) {
                        throw new ValidatorFileException(locale().malformedUrl(urlNode.asText(), baseUrl, activeDesc.getName()));
                    }
                }
            }
        }
        return resources.toArray(new TableResource[0]);
    }

    /**
     * Creates a string that graphically shows the dependencies in the full validation tree;
     * @return graphical representation of the tree
     * @throws CheckCreationException If the full validation tree cannot be created.
     */
    public String printFullValidationTree() throws CheckCreationException {
        SingletonCheckFactory scf = new SingletonCheckFactory(null, null);
        FullRootCheck rootCheck = scf.getInstance(FullRootCheck.class);
        return rootCheck.printTree();
    }
}
