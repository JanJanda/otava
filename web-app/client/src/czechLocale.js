export const czechLocale = {
  noValidation: "Tato žádost o validaci neexistuje.",
  queueing: "Žádost o validaci čeká ve frontě. Načtěte tuto stránku později.",
  setValLang: "Jazyk výsledku validace",
  english: "Angličtina",
  czech: "Čeština",
  valStyle: "Validační styl",
  fullVal: "Úplná validace",
  tablesVal: "Validace jen tabulek",
  descVal: "Validace jen deskriptorů",
  passTables: "Pasivní tabulky",
  activeTables: "Aktivní tabulky",
  passDescs: "Pasivní deskriptory",
  activeDescs: "Aktivní deskriptory",
  description: "Popis žádosti",
  submit: "Odeslat žádost o validaci",
  validation: "Validace",
  reqTime: "Čas žádosti",
  reqDetail: "Detaily žádosti",
  langTag: "cs",
  working: "Validace právě probíhá. Načtěte tuto stránku později.",
  finished: "Validace je dokončena.",
  report: "Validační zpráva",
  finishTime: "Čas dokončení",
  notFound: "Smůla, stránka nenalezena",
  searchById: "Hledat validaci podle ID",
  search: "Hledat",
  validate: "Validovat",
  searchValidation: "Hledat validaci",
  otava: "Otevřený validátor tabulek",
  webApp: "webová aplikace",
  introText: "Tato webová aplikace je součástí většího projektu OTAVA. " +
    "Projekt OTAVA obsahuje software pro validaci CSV tabulek společně se soubory jejich metadatových deskriptorů podle doporučení CSV na Webu od organizace W3C. " +
    "Doporučení CSV na Webu určuje, jak popsat CSV soubory publikované na webu za použití JSON-LD deskriptorů s důležitými metadaty, jako názvy sloupců, datové typy a další. " +
    "Jádrem projektu je knihovna v jazyce Java s hlavní funkcionalitou. " +
    "Projekt také obsahuje aplikaci příkazové řádky v jazyce Java, která poskytuje přístup ke každé schopnosti knihovny. " +
    "Tato webová aplikace poskytuje zjednodušené rozhraní s omezenými schopnostmi pro méně zkušené uživatele.",
  manual: "Návod k použití",
  howToUse: "Uživatelé podávají své žádosti o validace prostřednictvím webového formuláře. " +
    "Expertní formulář nabízí tři validační styly. " +
    "Úplná validace validuje všechny tabulky a deskriptory dohromady. " +
    "Validace jen tabulek validuje jen samotné pasivní tabulky. " +
    "Validace jen deskriptorů validuje jen samotné pasivní deskriptory. " +
    "Jsou zde čtyři typy dokumentů: pasivní tabulka, aktivní tabulka, pasivní deskriptor a aktivní deskriptor. " +
    "Aktivní tabulka se pokusí lokalizovat svůj deskriptor, pasivní tabulka nikoli. " +
    "Aktivní deskriptor se pokusí lokalizovat své tabulky, pasivní deskriptor nikoli. " +
    "Každý dokument má URL se skutečným umístěním souboru. " +
    "Každý dokument může mít alias. " +
    "Alias je alternativní URL. " +
    "Pokud je alias přítomen, pak je použit během validace místo skutečného URL. " +
    "Dokument je nalezen a nahrán podle svého URL, ale předstírá, že se nachází na jiném umístění podle svého aliasu. " +
    "Ve formuláři je textové vstupní pole pro každý typ dokumentu. " +
    "Každá řádka v poli reprezentuje jeden dokument a obsahuje nejprve URL dokumentu a poté nepovinně alias dokumentu oddělený mezerou. " +
    "Pokud chcete validovat lokální soubor na svém počítači, musíte ho nejprve nahrát někam na web pomocí vámi vybrané webové služby. " +
    "Nejsnazší možnost je asi GitHub. " +
    "Alternativně můžete použít aplikaci příkazové řádky v jazyce Java. " +
    "Po dokončení validace je možné vyhledat výsledky validace podle přiděleného ID. " +
    "Každá validační žádost a výsledky zůstanou online přibližně sedm dnů a poté jsou smazány.",
  about: "O aplikaci",
  descUrl: "URL deskriptoru",
  switchActiveDesc: "Najít a použít tabulky z deskriptoru",
  tableUrl: "URL tabulky",
  switchActiveTable: "Najít a použít deskriptor tabulky",
  tableValidation: "Validace tabulky",
  descValidation: "Validace deskriptoru",
  expertValidation: "Expertní validace",
  chooseVal: "Vyberte si svou validaci",
  projectGitHub: "Tento projekt na GitHubu"
};
