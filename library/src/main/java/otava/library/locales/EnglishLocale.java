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
    public String checkCreationFail(String typeName) {
        return "The check " + typeName + " cannot be created.";
    }

    @Override
    public String badContext(String descriptorName) {
        return "Descriptor " + descriptorName + " has incorrect field @context.";
    }

    @Override
    public String missingDesc(String tableName) {
        return "Missing descriptor for the table " + tableName;
    }

    @Override
    public String missingTable(String url) {
        return "Missing described table with URL " + url;
    }

    @Override
    public String malformedUrl(String url, String base, String descName) {
        return "Cannot resolve URL " + url + " against base " + base + " in descriptor " + descName;
    }

    @Override
    public String checkRunException(String checkName) {
        return "Unexpected exception in check " + checkName;
    }

    @Override
    public String emptyTable(String tableName) {
        return "Table " + tableName + " is empty.";
    }

    @Override
    public String missingColDesc(String colName, String tableName) {
        return "Column " + colName + " in table " + tableName + " does not have a description.";
    }

    @Override
    public String missingCol(String colName, String descName) {
        return "Column " + colName + " is not in described table " + descName;
    }

    @Override
    public String badValueInPrimKey(String tableUrl) {
        return "Invalid value in primary key definition for the table with URL " + tableUrl;
    }

    @Override
    public String missingPrimKeyTitles(String colName, String tableUrl) {
        return "Cannot find titles of primary key column with name " + colName + " for the table with URL " + tableUrl;
    }
}
