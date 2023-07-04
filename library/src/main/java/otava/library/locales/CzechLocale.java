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
    public String malformedUrl(String url, String base, String descName) {
        return "Nelze vytvořit absolutní URL z " + url + " vzhledem k bázi " + base + " v dekriptoru " + descName;
    }

    @Override
    public String checkRunException(String checkName) {
        return "Neočekávaná výjimka ve validační kontrole " + checkName;
    }

    @Override
    public String emptyTable(String tableName) {
        return "Tabulka " + tableName + " je prázdná.";
    }

    @Override
    public String missingColDesc(String colName, String tableName) {
        return "Sloupec " + colName + " v tabulce " + tableName + " nemá popis v metadatech.";
    }

    @Override
    public String missingCol(String colName, String descName) {
        return "Sloupec " + colName + " chibí v popsané tabulce " + descName;
    }
}
