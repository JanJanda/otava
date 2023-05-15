package otava.library;

final class Tables {
    private final Table[] tables;

    public Tables(Table[] tables) {
        this.tables = tables;
    }

    public Table getTable(int index) {
        return tables[index];
    }
}
