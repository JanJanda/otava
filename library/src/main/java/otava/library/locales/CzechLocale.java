package otava.library.locales;

public final class CzechLocale implements Locale {
    @Override
    public String badLineSeparators(String tableName) {
        return "Tabulka " + tableName + " má špatné oddělovače řádků.";
    }

    @Override
    public String ioException() {
        return "Chyba při čtení souboru.";
    }

    @Override
    public String missingFile(String fileName) {
        return "Chybí soubor " + fileName;
    }

    @Override
    public String cyclicalDeps() {
        return "Závislosti validačních kontrol jsou cyklické.";
    }

    @Override
    public String badCtor(String typeName) {
        return "Validační kontrola " + typeName + " nemá očekávaný konstruktor.";
    }

    @Override
    public String badContext(String descriptorName) {
        return "Deskriptor " + descriptorName + " má špatnou položku @context.";
    }

    @Override
    public String missingDesc(String tableName) {
        return "Chybí deskriptor pro tabulku " + tableName;
    }
}
