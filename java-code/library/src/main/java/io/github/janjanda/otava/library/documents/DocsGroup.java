package io.github.janjanda.otava.library.documents;

import java.util.Arrays;
import java.util.Iterator;

public final class DocsGroup<T extends Document> implements Iterable<T> {
    private final T[] documents;
    public final int count;

    public DocsGroup(T[] documents) {
        this.documents = documents.clone();
        count = this.documents.length;
    }

    public T getDocument(int index) {
        return documents[index];
    }

    public T[] getDocuments() {
        return documents.clone();
    }

    @Override
    public Iterator<T> iterator() {
        return Arrays.stream(documents).iterator();
    }
}
