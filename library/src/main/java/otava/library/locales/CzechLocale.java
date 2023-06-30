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
    public String checkCreationFail(String typeName) {
        return "Validační kontrola " + typeName + " nemůže být vytvořena.";
    }

    @Override
    public String badContext(String descriptorName) {
        return "Deskriptor " + descriptorName + " má špatnou položku @context.";
    }

    @Override
    public String missingDesc(String tableName) {
        return "Chybí deskriptor pro tabulku " + tableName;
    }

    @Override
    public String missingTable(String url) {
        return "Chybí popsaná tabulka s URL " + url;
    }

    @Override
    public String checkRunEx(String checkName) {
        return "Neočekávaná výjimka ve validační kontrole " + checkName;
    }
}
