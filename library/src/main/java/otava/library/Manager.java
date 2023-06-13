package otava.library;

import otava.library.checks.*;
import otava.library.documents.*;
import otava.library.locales.*;
import otava.library.results.Result;

public final class Manager {
    private static Locale currentLocale = new EnglishLocale();

    public Manager() {}

    public Manager(Locale locale) {
        currentLocale = locale;
    }

    public static Locale locale() {
        return currentLocale;
    }

    public Result validateLocalTable(String tablePath) throws ValidatorException {
        DocumentFactory documentFactory = new DocumentFactory();
        LocalTable table = documentFactory.getLocalTable(tablePath);
        Documents<Table> tables = new Documents<>(new Table[]{table});
        SingletonCheckFactory scf = new SingletonCheckFactory(tables, null);
        LineBreaksCheck validator = scf.getInstance(LineBreaksCheck.class);
        return validator.validate();
    }
}
