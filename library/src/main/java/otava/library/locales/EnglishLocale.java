package otava.library.locales;

public final class EnglishLocale implements Locale {
    @Override
    public String badLineSeparators(String tableName) {
        return "Table " + tableName + " has incorrect line breaks.";
    }

    @Override
    public String ioException() {
        return "Error when reading file.";
    }

    @Override
    public String missingFile(String fileName) {
        return "File " + fileName + " is missing.";
    }

    @Override
    public String cyclicalDeps() {
        return "Pre-check dependencies are cyclical.";
    }

    @Override
    public String badCtor(String typeName) {
        return "The check " + typeName + " does not have the expected constructor.";
    }

    @Override
    public String badContext(String descriptorName) {
        return "Descriptor " + descriptorName + " has incorrect field @context.";
    }
}
