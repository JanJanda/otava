package otava.library;

import java.util.Arrays;
import java.util.Iterator;

public final class Tables implements Iterable<Table> {
    private final Table[] tables;
    public final int length;

    public Tables(Table... tables) {
        this.tables = tables;
        length = tables.length;
    }

    public Table getTable(int index) {
        return tables[index];
    }

    @Override
    public Iterator<Table> iterator() {
        return Arrays.stream(tables).iterator();
    }
}
