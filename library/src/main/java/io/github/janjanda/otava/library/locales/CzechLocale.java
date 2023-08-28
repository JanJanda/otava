package io.github.janjanda.otava.library.locales;

public final class CzechLocale implements Locale {
    @Override
    public String langTag() {
        return "cs";
    }

    @Override
    public String badLineSeparators(String tableName) {
        return "Tabulka " + tableName + " má špatné oddělovače řádků.";
    }

    @Override
    public String ioException(String fileName) {
        return "Chyba při čtení souboru " + fileName;
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
        return "Sloupec " + colName + " chybí v popsané tabulce " + descName;
    }

    @Override
    public String badValueInPrimKey(String tableUrl) {
        return "Neplatná hodnota v definici primárního klíče pro tabulku s URL " + tableUrl;
    }

    @Override
    public String missingPrimKeyTitles(String colName, String tableUrl) {
        return "Nelze najít názvy pro sloupec primárního klíče jménem " + colName + " pro tabulku s URL " + tableUrl;
    }

    @Override
    public String invalidPrimKey(String tableName) {
        return "Primární klíč nemá unikátní hodnoty v tabulce " + tableName;
    }

    @Override
    public String emptyRequiredColumn(String tableName) {
        return "Požadovaný sloupec v tabulce " + tableName + " má prázdné buňky.";
    }

    @Override
    public String fatal() {
        return "fatální";
    }

    @Override
    public String skipped() {
        return "vynechaný";
    }

    @Override
    public String badDatatype(String tableName, String row, String column) {
        return "Nesprávná hodnota pro datový typ v tabulce " + tableName + " řádek " + row + " sloupec " + column + ".";
    }

    @Override
    public String badRowSize(String tableName, String row) {
        return "Řádek " + row + " v tabulce " + tableName + " má chybný počet buněk.";
    }

    @Override
    public String badColRefInFKey(String tableUrl) {
        return "Chybná hodnota v column reference v cizím klíči v tabulce s URL " + tableUrl;
    }

    @Override
    public String missingFKeyColDesc(String name, String tableUrl) {
        return "Nelze najít popis pro referencovaný sloupec cizího klíče se jménem " + name + " pro tabulku s URL " + tableUrl;
    }

    @Override
    public String badRefTableUrl(String url, String tableUrl) {
        return "Nelze rezolvovat URL " + url + " v cizím klíči v tabulce s URL " + tableUrl;
    }

    @Override
    public String missingFKeyTable(String tableUrl) {
        return "Chybí referencovaná tabulka v cizím klíči v tabulce s URL " + tableUrl;
    }

    @Override
    public String badNumOfFKeyCols(String tableUrl) {
        return "Musí být stejný počet platných sloupců a referencovaných sloupců v cizím klíči v tabulce s URL " + tableUrl;
    }

    @Override
    public String fKeyViolation(String tableUrl, String refTableUrl) {
        return "Cizí klíč je porušen v tabulce " + tableUrl + " protože příslušné hodnoty chybí v referencované tabulce " + refTableUrl;
    }

    @Override
    public String emptyTablesArray(String descName) {
        return "Pole tables je prázdné v deskriptoru " + descName;
    }

    @Override
    public String invalidId(String descName) {
        return "Je zde špatné @id v deskriptoru " + descName;
    }

    @Override
    public String badType(String descName, String expected) {
        return "Špatný typ v deskriptoru " + descName + " - měl by být " + expected;
    }

    @Override
    public String missingTablesArray(String descName) {
        return "Chybí požadovaná hodnota tables v deskriptoru " + descName;
    }

    @Override
    public String missingTableUrl(String descName) {
        return "Chybí URL pro tabulku v deskriptoru " + descName;
    }

    @Override
    public String propIsNotArray(String propName, String descName) {
        return "Položka " + propName + " má být pole v deskriptoru " + descName;
    }

    @Override
    public String propIsNotObject(String propName, String descName) {
        return "Položka " + propName + " má být objekt v deskriptoru " + descName;
    }

    @Override
    public String propBadType(String propName, String descName) {
        return "Položka " + propName + " má špatný typ v deskriptoru " + descName;
    }

    @Override
    public String virtBeforeNonVirt(String descName) {
        return "Virtuální sloupec je úmístěn před nevirtuálním sloupcem v deskriptoru " + descName;
    }

    @Override
    public String extraContext(String descName) {
        return "Je tu @context navíc v dekriptoru " + descName;
    }
}
