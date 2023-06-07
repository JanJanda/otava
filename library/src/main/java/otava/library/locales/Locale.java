package otava.library.locales;

public interface Locale {
    String badLineSeparators(String tableName);
    String ioException();
    String missingFile(String fileName);
    String cyclicalDeps();
    String badCtor(String typeName);
}
