package otava.library;

import otava.library.checks.*;

final class Manager {
    public Result validateLocalTable(String tablePath) throws ValidatorException {
        DocumentFactory documentFactory = new DocumentFactory();
        LocalTable table = documentFactory.getLocalTable(tablePath);
        Tables tables = new Tables(table);
        SingletonCheckFactory scf = new SingletonCheckFactory(tables, null);
        LineBreaksCheck validator = scf.getInstance(LineBreaksCheck.class);
        return validator.validate();
    }
}
