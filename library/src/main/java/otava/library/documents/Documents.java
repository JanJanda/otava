package otava.library.documents;

import java.util.Arrays;
import java.util.Iterator;

public final class Documents <T extends Document> implements Iterable<T> {
    private final T[] documents;
    public final int count;

    public Documents(T[] documents) {
        this.documents = documents;
        count = this.documents.length;
    }

    public T getDocument(int index) {
        return documents[index];
    }

    @Override
    public Iterator<T> iterator() {
        return Arrays.stream(documents).iterator();
    }
}
