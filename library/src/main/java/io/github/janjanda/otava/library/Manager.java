package io.github.janjanda.otava.library;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.janjanda.otava.library.checks.Check;
import io.github.janjanda.otava.library.checks.ConsistentColumnsCheck;
import io.github.janjanda.otava.library.checks.FullRootCheck;
import io.github.janjanda.otava.library.checks.VirtualsLastCheck;
import io.github.janjanda.otava.library.documents.Descriptor;
import io.github.janjanda.otava.library.documents.DocsGroup;
import io.github.janjanda.otava.library.documents.Document;
import io.github.janjanda.otava.library.documents.Table;
import io.github.janjanda.otava.library.exceptions.CheckCreationException;
import io.github.janjanda.otava.library.exceptions.ValidatorException;
import io.github.janjanda.otava.library.exceptions.ValidatorFileException;
import io.github.janjanda.otava.library.factories.DocumentFactory;
import io.github.janjanda.otava.library.factories.SingletonCheckFactory;
import io.github.janjanda.otava.library.locales.EnglishLocale;
import io.github.janjanda.otava.library.locales.Locale;

import java.net.MalformedURLException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static io.github.janjanda.otava.library.ValidationSuite.DescResource;
import static io.github.janjanda.otava.library.ValidationSuite.TableResource;
import static io.github.janjanda.otava.library.utils.DescriptorUtils.extractTables;
import static io.github.janjanda.otava.library.utils.UrlUtils.getBaseUrlWithExc;
import static io.github.janjanda.otava.library.utils.UrlUtils.resolveUrl;

/**
 * This is the main class of the library. It provides unified interface for the use of the library.
 */
public final class Manager {
    public static final String rdfPrefix = "https://janjanda.github.io/otava/";
    private static Locale currentLocale = new EnglishLocale();
    private static boolean localeLocked = false;
    private static final Class<? extends Check> tablesValidation = ConsistentColumnsCheck.class;
    private static final Class<? extends Check> descriptorsValidation = VirtualsLastCheck.class;
    private static final Class<? extends Check> fullRootValidation = FullRootCheck.class;

    /**
     * Gets the value of the static variable with the current locale. The locale is in a static variable, and it affects the entire program.
     * @return current locale
     */
    public static Locale locale() {
        return currentLocale;
    }

    /**
     * Sets the locale of the library. This method must be called before an instance of this class is created, otherwise the locale is not set.
     * @param locale the locale to set
     * @return {@code true} if the locale was successfully set, {@code false} otherwise
     */
    public static boolean setLocale(Locale locale) {
        if (!localeLocked) {
            currentLocale = locale;
            return true;
        }
        return false;
    }

    /**
     * Locale cannot be set after this constructor is called.
     */
    public Manager() {
        localeLocked = true;
    }

    /**
     * Validates only tables without descriptors. Only passive tables from the validation suite are validated.
     * @param validationSuite specification of validated files, only passive tables are read
     * @return results of individual validation checks
     * @throws ValidatorException If the validation cannot be performed.
     */
    public Report tablesOnlyValidation(ValidationSuite validationSuite) throws ValidatorException {
        Instant start = Instant.now();
        DocumentFactory df = new DocumentFactory();
        List<Table> tables = createTables(validationSuite.getPassiveTables(), df);
        DocsGroup<Table> tableGroup = new DocsGroup<>(tables.toArray(new Table[0]));
        SingletonCheckFactory scf = new SingletonCheckFactory(tableGroup, null);
        Check check = scf.getInstance(tablesValidation);
        check.validate();
        Set<Result> results = check.getAllResults();
        Instant end = Instant.now();
        return new Report(Duration.between(start, end), extractDocumentNames(tableGroup), new String[0], makeSortedResults(results));
    }

    /**
     * Validate only descriptors without tables. Only passive descriptors from the validation suite are validated.
     * @param validationSuite specification of validate files, only passive descriptors are read
     * @return results of individual validation checks
     * @throws ValidatorException If the validation cannot be performed.
     */
    public Report descriptorsOnlyValidation(ValidationSuite validationSuite) throws ValidatorException {
        Instant start = Instant.now();
        DocumentFactory df = new DocumentFactory();
        List<Descriptor> descriptors = createDescriptors(validationSuite.getPassiveDescriptors(), df);
        DocsGroup<Descriptor> descGroup = new DocsGroup<>(descriptors.toArray(new Descriptor[0]));
        SingletonCheckFactory scf = new SingletonCheckFactory(null, descGroup);
        Check check = scf.getInstance(descriptorsValidation);
        check.validate();
        Set<Result> results = check.getAllResults();
        Instant end = Instant.now();
        return new Report(Duration.between(start, end), new String[0], extractDocumentNames(descGroup), makeSortedResults(results));
    }

    /**
     * Performs full validation with the provided validation suite.
     * @param validationSuite specifications of validated files
     * @return results of individual validation checks
     * @throws ValidatorException If the validation cannot be performed.
     */
    public Report fullValidation(ValidationSuite validationSuite) throws ValidatorException {
        Instant start = Instant.now();
        DocumentFactory df = new DocumentFactory();
        List<Table> tables = createTables(validationSuite.getActiveTables(), df);
        List<Descriptor> descriptors = createDescriptors(validationSuite.getActiveDescriptors(), df);
        DescResource[] descResources = createDescResources(tables);
        TableResource[] tableResources = createTableResources(descriptors, validationSuite.saveMemory);
        tables.addAll(createTables(tableResources, df));
        descriptors.addAll(createDescriptors(descResources, df));
        tables.addAll(createTables(validationSuite.getPassiveTables(), df));
        descriptors.addAll(createDescriptors(validationSuite.getPassiveDescriptors(), df));
        DocsGroup<Table> tableGroup = new DocsGroup<>(tables.toArray(new Table[0]));
        DocsGroup<Descriptor> descGroup = new DocsGroup<>(descriptors.toArray(new Descriptor[0]));
        SingletonCheckFactory scf = new SingletonCheckFactory(tableGroup, descGroup);
        Check check = scf.getInstance(fullRootValidation);
        check.validate();
        Set<Result> results = check.getAllResults();
        Instant end = Instant.now();
        return new Report(Duration.between(start, end), extractDocumentNames(tableGroup), extractDocumentNames(descGroup), makeSortedResults(results));
    }

    private List<Table> createTables(TableResource[] specs, DocumentFactory df) throws ValidatorFileException {
        List<Table> tables = new ArrayList<>();
        for (TableResource spec : specs) {
            if (spec.isLocal()) {
                if (spec.outOfMemory()) tables.add(df.makeLocalOutOfMemoryTable(spec.name(), spec.alias()));
                else tables.add(df.makeLocalInMemoryTable(spec.name(), spec.alias()));
            }
            else {
                if (spec.outOfMemory()) tables.add(df.makeOnlineOutOfMemoryTable(spec.name(), spec.alias()));
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

    private TableResource[] createTableResources(List<Descriptor> activeDescs, boolean outOfMemory) throws ValidatorFileException {
        List<TableResource> resources = new ArrayList<>();
        for (Descriptor activeDesc : activeDescs) {
            String baseUrl;
            try {
                baseUrl = getBaseUrlWithExc(activeDesc);
            }
            catch (MalformedURLException e) {
                throw new ValidatorFileException(locale().cannotResolveBase(activeDesc.getName()) + " --- " + e.getMessage());
            }
            List<JsonNode> extractedTables = extractTables(activeDesc);
            for (JsonNode extractedTable : extractedTables) {
                JsonNode urlNode = extractedTable.path("url");
                if (urlNode.isTextual()) {
                    try {
                        String url = resolveUrl(baseUrl, urlNode.asText());
                        resources.add(new TableResource(url, null, false, outOfMemory));
                    }
                    catch (MalformedURLException e) {
                        throw new ValidatorFileException(locale().malformedUrl(urlNode.asText(), baseUrl, activeDesc.getName()) + " --- " + e.getMessage());
                    }
                }
            }
        }
        return resources.toArray(new TableResource[0]);
    }

    private Result[] makeSortedResults(Set<Result> resultSet) {
        Result[] results = resultSet.toArray(new Result[0]);
        Arrays.sort(results, Comparator.comparing(a -> a.originCheck));
        return results;
    }

    private String[] extractDocumentNames(DocsGroup<?> docsGroup) {
        Document[] docs = docsGroup.getDocuments();
        String[] names = new String[docs.length];
        for (int i = 0; i < docs.length; i++) {
            names[i] = docs[i].getPreferredName();
        }
        return names;
    }

    /**
     * Creates a string that graphically shows the dependencies in the tables only validation tree.
     * @return graphical representation of the tree
     */
    public static String printTablesOnlyValidationTree() {
        return printValidationTree(tablesValidation);
    }

    /**
     * Creates a string that graphically shows the dependencies in the descriptors only validation tree.
     * @return graphical representation of the tree
     */
    public static String printDescriptorsOnlyValidationTree() {
        return printValidationTree(descriptorsValidation);
    }

    /**
     * Creates a string that graphically shows the dependencies in the full validation tree.
     * @return graphical representation of the tree
     */
    public static String printFullValidationTree() {
        return printValidationTree(fullRootValidation);
    }

    private static String printValidationTree(Class<? extends Check> checkClass) {
        try {
            SingletonCheckFactory scf = new SingletonCheckFactory(null, null);
            Check check = scf.getInstance(checkClass);
            return check.printTree();
        }
        catch (CheckCreationException e) {
            return "Cannot create the validation tree!";
        }
    }
}
