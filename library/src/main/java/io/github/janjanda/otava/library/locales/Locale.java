package io.github.janjanda.otava.library.locales;

public interface Locale {
    String badLineSeparators(String tableName);
    String ioException();
    String missingFile(String fileName);
    String checkCreationFail(String typeName);
    String badContext(String descriptorName);
    String missingDesc(String tableName);
    String missingTable(String url);
    String malformedUrl(String url, String base, String descName);
    String checkRunException(String checkName);
    String emptyTable(String tableName);
    String missingColDesc(String colName, String tableName);
    String missingCol(String colName, String descName);
    String badValueInPrimKey(String tableUrl);
    String missingPrimKeyTitles(String colName, String tableUrl);
    String invalidPrimKey(String tableName);
    String emptyRequiredColumn(String tableName);
    String fatal();
    String skipped();
    String badDatatype(String tableName, String row, String colIndex);
}
