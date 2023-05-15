package otava.library;

public final class Tables {
    private final Table[] tables;
    public final int length;

    public Tables(Table[] tables) {
        this.tables = tables;
        length = tables.length;
    }

    public Table getTable(int index) {
        return tables[index];
    }
}
