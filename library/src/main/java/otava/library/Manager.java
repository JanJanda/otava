package otava.library;

import otava.library.checks.*;
import otava.library.documents.*;
import otava.library.exceptions.ValidatorException;
import otava.library.locales.*;
import java.io.IOException;

public final class Manager {
    private static Locale currentLocale = new EnglishLocale();

    public Manager() {}

    public Manager(Locale locale) {
        currentLocale = locale;
    }

    public static Locale locale() {
        return currentLocale;
    }

    public Result validateLocalTable(String tablePath) throws ValidatorException, IOException {
        DocumentFactory documentFactory = new DocumentFactory();
        LocalInMemoryTable table = documentFactory.getLocalTable(tablePath, null);
        DocsGroup<Table> tables = new DocsGroup<>(new Table[]{table});
        SingletonCheckFactory scf = new SingletonCheckFactory(tables, null);
        LineBreaksCheck validator = scf.getInstance(LineBreaksCheck.class);
        return validator.validate();
    }
}
