package otava.library;

import otava.library.checks.*;
import otava.library.locales.*;

public final class Manager {
    private static Locale locale = new EnglishLocale();

    public Manager() {}

    public Manager(Locale locale) {
        Manager.locale = locale;
    }

    public static Locale locale() {
        return locale;
    }

    public Result validateLocalTable(String tablePath) throws ValidatorException {
        DocumentFactory documentFactory = new DocumentFactory();
        LocalTable table = documentFactory.getLocalTable(tablePath);
        Tables tables = new Tables(table);
        SingletonCheckFactory scf = new SingletonCheckFactory(tables, null);
        LineBreaksCheck validator = scf.getInstance(LineBreaksCheck.class);
        return validator.validate();
    }
}
