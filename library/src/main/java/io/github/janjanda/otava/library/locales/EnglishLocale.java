package io.github.janjanda.otava.library.locales;

public final class EnglishLocale implements Locale {
    @Override
    public String badLineSeparators(String tableName) {
        return "Table " + tableName + " has incorrect line breaks.";
    }

    @Override
    public String ioException(String fileName) {
        return "Error when reading the file " + fileName;
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
        return "Column " + colName + " in table " + tableName + " does not have any description.";
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

    @Override
    public String invalidPrimKey(String tableName) {
        return "Primary key does not have unique values in the table " + tableName;
    }

    @Override
    public String emptyRequiredColumn(String tableName) {
        return "Required column in the table " + tableName + " has empty cells.";
    }

    @Override
    public String fatal() {
        return "fatal";
    }

    @Override
    public String skipped() {
        return "skipped";
    }

    @Override
    public String badDatatype(String tableName, String row, String column) {
        return "Incorrect value for data type in the table " + tableName + " row " + row + " column " + column + ".";
    }

    @Override
    public String badRowSize(String tableName, String row) {
        return "Row " + row + " in the table " + tableName + " has incorrect number of cells.";
    }

    @Override
    public String badColRefInFKey(String tableUrl) {
        return "Incorrect value in column reference in foreign key in the table with URL " + tableUrl;
    }

    @Override
    public String missingFKeyColDesc(String name, String tableUrl) {
        return "Cannot find description for foreign key reference column with name " + name + " for the table with URL " + tableUrl;
    }

    @Override
    public String badRefTableUrl(String url, String tableUrl) {
        return "Cannot resolve URL " + url + " in the foreign key in the table with URL " + tableUrl;
    }

    @Override
    public String missingFKeyTable(String tableUrl) {
        return "Missing resource table in the foreign key in the table with URL " + tableUrl;
    }

    @Override
    public String badNumOfFKeyCols(String tableUrl) {
        return "There must be the same number of valid columns and referenced columns in the foreign key in the table with URL " + tableUrl;
    }

    @Override
    public String fKeyViolation(String tableUrl, String refTableUrl) {
        return "Foreign key violated in the table " + tableUrl + " because values are missing in the referenced table " + refTableUrl;
    }
}
